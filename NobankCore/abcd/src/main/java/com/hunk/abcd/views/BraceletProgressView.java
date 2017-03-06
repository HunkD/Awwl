package com.hunk.abcd.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.hunk.abcd.R;
import com.hunk.abcd.extension.util.ViewHelper;

/**
 * @author HunkDeng
 * @since 2017/3/6
 */
public class BraceletProgressView extends View {
    private final Paint circlePaint;
    private final Paint progressDefaultPaint;
    private final Paint progressPaint;
    private final float padding;

    private int value = 75;
    private RectF rect;

    public BraceletProgressView(Context context) {
        this(context, null);
    }

    public BraceletProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BraceletProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        @ColorInt int color = ContextCompat.getColor(context, R.color.green_bracelet);
        circlePaint.setColor(color);
        circlePaint.setShadowLayer(18, 2, 1, color);
        // disable hardware accelerate
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        progressDefaultPaint = new Paint();
        progressDefaultPaint.setStyle(Paint.Style.STROKE);
        progressDefaultPaint.setColor(ContextCompat.getColor(context, R.color.green_bracelet_default));
        progressDefaultPaint.setStrokeWidth(ViewHelper.pxFromDp(context, 3f));
        padding = ViewHelper.pxFromDp(context, 8f);

        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setColor(color);
        progressPaint.setStrokeWidth(ViewHelper.pxFromDp(context, 6f));
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            rect = new RectF();
            rect.left = left + padding;
            rect.top = top + padding;
            rect.right = right - padding;
            rect.bottom = bottom - padding;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, (float) (getMeasuredWidth()/2 - 30 * 1.5), circlePaint);
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, getMeasuredWidth()/2 - padding, progressDefaultPaint);
        canvas.drawArc(rect, -90, (float) value / 100 * 360, false, progressPaint);
    }

    public void setValue(int value) {
        this.value = value;
    }
}
