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
import android.widget.Space;

import com.hunk.abcd.extension.log.Logging;
import com.hunk.nobank.R;

/**
 * @author HunkDeng
 * @since 2017/2/18
 */
public class DashboardTop extends CoordinatorLayout {

    private final Space space;
    private final View topLayer;
    private final View bottomLayer;
    private final View banner;
    private int originalHeight;
    private int currentHeight;
    private boolean isExpand = true;
    // scroll param
    private float lastY;
    /**
     * Use this field to track the handler for this time touch event chain
     * We try to let dashboardTop or dashboardScrollView to handle touch event
     */
    private boolean isHandle;

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
        topLayer = findViewById(R.id.top_layer);
        bottomLayer = findViewById(R.id.bottom_layer);
        banner = findViewById(R.id.banner);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                originalHeight = getMeasuredHeight();
                currentHeight = getMeasuredHeight();
                // set space
                space.getLayoutParams().height = originalHeight - banner.getMeasuredHeight();
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

    public boolean isExpand() {
        return isExpand;
    }

    public boolean move(float distance, boolean b) {
//        if (state != Animating) {
        if (distance > 0) { // scroll down motion
            if (currentHeight == originalHeight) { // view has expanded to the limitation height.
                return false;
            } else {
                // expand the view
                changeHeight(distance);
                return true;
            }
        } else { // scroll up motion
            if (currentHeight == banner.getMeasuredHeight()) {
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
        } else if (layoutParams.height < banner.getMeasuredHeight()) { // exceed min height(banner's height)
            layoutParams.height = banner.getMeasuredHeight();
        }
        currentHeight = layoutParams.height;
        space.getLayoutParams().height = currentHeight - banner.getMeasuredHeight();
        setLayoutParams(layoutParams);
        requestLayout();
    }

    public boolean handleMotionEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Logging.d("ACTION_DOWN");
                lastY = ev.getY();
                isHandle = false;
                return false;
            }
            case MotionEvent.ACTION_MOVE: {
                // TODO: if dashboardTop is first element in the visible view
                // then use dashboardTop.move to check whether it will handle the move event
                // if not, scroll view handle the event then.
                float distance = ev.getY() - lastY;
                lastY = ev.getY();
                if (move(distance, true)) {
                    isHandle = true;
                }
                return isHandle;
            }
            case MotionEvent.ACTION_UP:
                return isHandle;
        }
        return false;
    }
}

