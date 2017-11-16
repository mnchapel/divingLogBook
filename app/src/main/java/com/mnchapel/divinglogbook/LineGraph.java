package com.mnchapel.divinglogbook;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Marie-Neige on 14/11/2017.
 */

public class LineGraph extends View {
    private Paint paint;
    private Paint polygonPaint;
    private List<DiveSample> diveSamples;
    private int sampleInterval;



    public LineGraph(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);

        polygonPaint = new Paint();
        polygonPaint.setStyle(Paint.Style.FILL);
        polygonPaint.setColor(getResources().getColor(R.color.blue));
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float xOrigin = 0;
        float yOrigin = 0;

        float scaleTime = 0.5f;
        float scaleDepth = 20;

        float maxDepth = yOrigin;

        float previousDepth = yOrigin;
        float previousTime = xOrigin;
        Path path = new Path();
        path.moveTo(xOrigin, yOrigin);

        for(DiveSample diveSample: diveSamples) {
            float depth = diveSample.getDepth();
            int time = diveSample.getTime();

            path.lineTo(time*scaleTime, depth*scaleDepth);

            canvas.drawLine(previousTime*scaleTime, previousDepth*scaleDepth, time*scaleTime, depth*scaleDepth, paint);

            maxDepth = (maxDepth>depth)?maxDepth:depth;

            previousDepth = depth;
            previousTime = time;
        }
        path.lineTo(previousTime, yOrigin);

        canvas.drawPath(path, polygonPaint);
        canvas.drawLine(xOrigin,yOrigin,xOrigin, maxDepth*scaleDepth, paint);
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
}
