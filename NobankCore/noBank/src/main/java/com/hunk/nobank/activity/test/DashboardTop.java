package com.hunk.nobank.activity.test;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Space;

import com.hunk.abcd.extension.log.Logging;
import com.hunk.nobank.R;

/**
 * @author HunkDeng
 * @since 2017/2/18
 */
public class DashboardTop extends CoordinatorLayout {

    private static final int DURATION_TIME = 800;
    private final Space space;
    private final ImageView topLayer;
    private final View bottomLayer;
    private final View banner;
    private int originalHeight;
    private int currentHeight;
    // scroll param
    private float lastY;
    //
    State state = State.Expand;
    /**
     * Use this field to track the handler for this time touch event chain
     * We try to let dashboardTop or dashboardScrollView to handle touch event
     */
    private boolean isHandle;
    private Scroller scroller;
    private int bannerMeasuredHeight;

    public DashboardTop(Context context) {
        this(context, null);
    }

    public DashboardTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_dashboard_top, this);
        space = (Space) findViewById(R.id.space);
        topLayer = (ImageView) findViewById(R.id.top_layer);
        bottomLayer = findViewById(R.id.bottom_layer);
        banner = findViewById(R.id.banner);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                originalHeight = getMeasuredHeight();
                currentHeight = getMeasuredHeight();
                bannerMeasuredHeight = banner.getMeasuredHeight();
                // set space
                space.getLayoutParams().height = originalHeight - bannerMeasuredHeight;
                space.requestLayout();
                // remove listener
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    public boolean move(float distance, boolean b) {
        if (distance > 0) { // scroll down motion
            if (currentHeight == originalHeight) { // view has expanded to the limitation height.
                return false;
            } else {
                // expand the view
                changeHeight(distance);
                return true;
            }
        } else { // scroll up motion
            if (currentHeight == bannerMeasuredHeight) {
                return false;
            } else {
                changeHeight(distance);
                return true;
            }
        }
    }

    /**
     * Change view height and also change first layer alpha and visibility
     * @param distance
     */
    private void changeHeight(float distance) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = (int) (currentHeight + distance);
        // protection handling
        if (layoutParams.height > originalHeight) { // exceed max height
            layoutParams.height = originalHeight;
            state = State.Expand;
        } else if (layoutParams.height < bannerMeasuredHeight) { // exceed min height(banner's height)
            layoutParams.height = bannerMeasuredHeight;
            state = State.Collapse;
        }
        currentHeight = layoutParams.height;
        // change top layer alpha when scroll
        float alpha = (float) (currentHeight - bannerMeasuredHeight) / (float) (originalHeight - bannerMeasuredHeight) * 255;
        topLayer.getDrawable().setAlpha((int) alpha);
        // change space height to leverage banner
        space.getLayoutParams().height = currentHeight - bannerMeasuredHeight;
        setLayoutParams(layoutParams);
        requestLayout();
    }

    public boolean handleMotionEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Logging.d("ACTION_DOWN");
                lastY = ev.getY();
                isHandle = false;
                scroller = null;
                return false;
            }
            case MotionEvent.ACTION_MOVE: {
                // then use dashboardTop.move to check whether it will handle the move event
                // if not, scroll view handle the event then.
                float distance = ev.getY() - lastY;
                lastY = ev.getY();
                if (move(distance, true)) {
                    state = State.Running;
                    isHandle = true;
                }
                return isHandle;
            }
            case MotionEvent.ACTION_UP:
                if (isHandle) {
                    startAnim();
                }
                return isHandle;
        }
        return false;
    }

    /**
     * if user stop drag screen, we will make top view back to Collapse/Expand state with animation
     */
    private void startAnim() {
        int currentMove = currentHeight - bannerMeasuredHeight;
        int originalMove = originalHeight - bannerMeasuredHeight;
        Logging.d("currentMove="+currentMove+",originalMove="+originalMove);
        scroller = new Scroller(getContext(), new AccelerateInterpolator());
        if (currentMove > originalMove / 2) {
            state = State.Expanding;
            scroller.startScroll(0, currentHeight, 0, originalHeight, DURATION_TIME);
        } else {
            state = State.Collapsing;
            scroller.startScroll(0, currentHeight, 0, bannerMeasuredHeight, DURATION_TIME);
        }
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller != null && scroller.computeScrollOffset()) {
            int dst = scroller.getCurrY();
            Logging.d("animation running, dst = " + dst + ", currentHeight = " + currentHeight);
            if (state == State.Collapsing) {
                changeHeight(currentHeight - dst);
            } else if (state == State.Expanding){
                changeHeight(dst - currentHeight);
            }
            if (currentHeight < originalHeight || currentHeight > bannerMeasuredHeight) {
                postInvalidate();
            }
        }
    }

    enum State {
        Expand, Expanding, Collapsing, Collapse, Running
    }
}