package com.hunk.nobank.extension.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.hunk.nobank.R;
import com.hunk.nobank.util.ViewHelper;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class NBProgressView extends View {
    private static final long TIME_PERIOD = 60;
    private final Paint mPaint;
    private final int mForeGroundLineColor;
    private final int mBackGroundLineColor;
    private final float mLineWidth;
    private final Timer mTimer;
    private final Handler mHandler;
    private RectF mRectF;
    private float mStart = 270;

    public NBProgressView(Context context) {
        this(context, null);
    }

    public NBProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NBProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.NBProgressView,
                0, 0);
        try {
            mForeGroundLineColor = a.getColor(R.styleable.NBProgressView_foregroundLine,
                    context.getResources().getColor(R.color.primary_dark));
            mBackGroundLineColor = a.getColor(R.styleable.NBProgressView_backgroundLine,
                    context.getResources().getColor(R.color.primary_text));
            mLineWidth = a.getDimension(R.styleable.NBProgressView_lineWidth, ViewHelper.pxFromDp(getContext(), 10f));
        } finally {
            a.recycle();
        }

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setPathEffect(new CornerPathEffect(10));
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);

        mTimer = new Timer();
        mHandler = new MyHandler(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRectF == null) {
            mRectF = new RectF(mLineWidth / 2, mLineWidth / 2,
                    getMeasuredWidth() - mLineWidth / 2,
                    getMeasuredHeight() - mLineWidth / 2);
        }
        startMyAnimation(false);
        // draw background line
        mPaint.setColor(mBackGroundLineColor);
        canvas.drawArc(mRectF, 0, 360, false, mPaint);
        // draw foreground line
        mPaint.setColor(mForeGroundLineColor);
        canvas.drawArc(mRectF, mStart%360, 90, false, mPaint);

        mStart += 8;
    }

    public void startMyAnimation(boolean forceStart) {
        if (forceStart) {
            postInvalidate();
        }
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendMessage(Message.obtain());
            }
        }, TIME_PERIOD, TIME_PERIOD);
    }

    public void stopAnimation() {
        mTimer.cancel();
    }

    private static class MyHandler extends Handler {
        private final WeakReference<NBProgressView> weakView;

        public MyHandler(NBProgressView nbProgressView) {
            weakView = new WeakReference<>(nbProgressView);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakView.get() != null) {
                weakView.get().postInvalidate();
            }
        }
    }
}
