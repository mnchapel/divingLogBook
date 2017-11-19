package com.mnchapel.divinglogbook;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.mnchapel.divinglogbook.com.mnchapel.divinglogbook.model.DiveSample;

import java.util.List;

/**
 * Created by Marie-Neige on 14/11/2017.
 */

public class LineGraph extends View {
    private Paint paint;
    private Paint polygonPaint;
    private List<DiveSample> diveSamples;
    private int sampleInterval;
    private Path background;



    /**
     * Constructor
     *
     * @param context:
     * @param attributeSet:
     */
    public LineGraph(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        background = new Path();

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);

        polygonPaint = new Paint();
        polygonPaint.setStyle(Paint.Style.FILL);
        polygonPaint.setColor(getResources().getColor(R.color.blue));
    }



    private float computeScaleTime(int canvasWidth) {
        int nbDiveSample = diveSamples.size();

        float a = (float)canvasWidth/(float)nbDiveSample;

        return (float)canvasWidth/(float)nbDiveSample;
    }



    /**
     *
     * @param canvas:
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        float xOrigin = 0;
        float yOrigin = 0;

        float scaleTime = computeScaleTime(canvasWidth);
        float scaleDepth = 30;

        float maxDepth = yOrigin;

        float previousDepth = yOrigin;
        float previousTime = xOrigin;

        background.reset();
        background.moveTo(xOrigin, yOrigin);

        for(DiveSample diveSample: diveSamples) {
            float depthScaled = diveSample.getDepth()*scaleDepth;
            float timeScaled = (diveSample.getTime()/sampleInterval)*scaleTime;

            background.lineTo(timeScaled, depthScaled);

            canvas.drawLine(previousTime, previousDepth, timeScaled, depthScaled, paint);

            maxDepth = (maxDepth>depthScaled)?maxDepth:depthScaled;

            previousDepth = depthScaled;
            previousTime = timeScaled;
        }
        background.lineTo(previousTime, yOrigin);
        background.lineTo(xOrigin, yOrigin);

        canvas.drawPath(background, polygonPaint);
        canvas.drawLine(xOrigin,yOrigin,xOrigin, maxDepth, paint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }



    public void setDiveSample(List<DiveSample> diveSamples) {
        this.diveSamples = diveSamples;
        this.invalidate();
    }



    public void setSampleInterval(int sampleInterval) {
        this.sampleInterval = sampleInterval;
        this.invalidate();
    }
}
