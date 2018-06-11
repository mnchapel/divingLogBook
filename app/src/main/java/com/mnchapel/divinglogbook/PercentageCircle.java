package com.mnchapel.divinglogbook;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Size;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * TODO: document your custom view class.
 */
public class PercentageCircle extends View {

    private static final int STROKE_WIDTH = 100;
    private Paint basePaint, degreesPaint;
    private RectF rectF;
    private int centerX, centerY, radius;
    private String mixtureType;
    private Size mixtureTypeSize;


    public PercentageCircle(Context context) {
        super(context);
        init(null, 0);
    }



    public PercentageCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }



    public PercentageCircle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }



    private void init(AttributeSet attrs, int defStyle) {
        basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basePaint.setStyle(Paint.Style.STROKE);
        basePaint.setStrokeWidth(STROKE_WIDTH);
        basePaint.setColor(ContextCompat.getColor(getContext(), R.color.blue));

        degreesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        degreesPaint.setStyle(Paint.Style.STROKE);
        degreesPaint.setStrokeWidth(STROKE_WIDTH);
        degreesPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (rectF == null)
        {
            TextView a = new TextView(getContext());

            String text = "Air";
            Rect bounds = new Rect();
            Paint textPaint = a.getPaint();
            textPaint.getTextBounds(text,0,text.length(),bounds);
            int mixtureTypeHeight = bounds.height();
            int mixtureTypeWidth = bounds.width();

            centerX = getMeasuredWidth()/ 2;
            centerY = getMeasuredHeight()/ 2;
            radius = Math.min(centerX,centerY);
            int padding = 40;

            int radius_bis = Math.max(mixtureTypeHeight/2, mixtureTypeWidth/2);
            double sum_squared = radius_bis*radius_bis + radius_bis*radius_bis;
            int radius_big = (int)Math.sqrt(sum_squared) + padding;

            int top    = centerY - radius_big - STROKE_WIDTH;
            int bottom = centerY + radius_big + STROKE_WIDTH;
            int left   = centerX - radius_big - STROKE_WIDTH;
            int right  = centerX + radius_big + STROKE_WIDTH;

            rectF = new RectF(left, top, right, bottom);
        }

        canvas.drawArc(rectF, 0, 360/*75.6f*/, false, degreesPaint);

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

//        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
//        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

//        int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
//        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

//        setMeasuredDimension(w, h);
    }



    /**
     *
     * @param hasFocus:
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
