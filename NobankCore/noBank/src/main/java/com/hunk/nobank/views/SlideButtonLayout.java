package com.hunk.nobank.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.hunk.nobank.R;
import com.hunk.nobank.util.Logging;

/**This view will provide a feature that allow user to slide the slide icon (which is the last view of the viewgroup).<br>
 * <p>
 * 1) If user slide the icon more than half of this view's width, then it will keep slide action until all 'reveal' view has been shown. <br>
 * 2) If user slide the icon less than half of this view's width, then it will fling a little bit and scroll back to origin state.
 * </p>
 * <p>
 * View Hierarchy
 * 1) reveal view<br>
 * 2) default mask<br>
 * 3) slide icon<br>
 * </p>
 */
public class SlideButtonLayout extends RelativeLayout {

    private static final int DURATION_TIME = 1 * 1000;
    // A icon we can hold and move it to reveal the slide view.
    private View mSlideIcon;
    // Local android scroller component
    private Scroller mScroller;
    // start point in x axis when we start to drag this view horizontally.
    private Integer mViewStartRawX;
    // start point to record where we start to scroll.
    private int mScrollStartX;
    // Describe the scroller state when we lift up our finger and trigger the action_up event.
    private State mState = State.STOP;
    //
    private OnTouchListener mSlideIconOnTouchListener = new OnTouchListener() {
        private VelocityTracker mVelocityTracker;
        // get first pointer id
        public int mStartPointerId;
        // current velocity record for x axis when we drag the slide view
        public int mCurrentXVelocity;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // If the view start scroll/fling, then we need disable touch event handling.
            if (State.STOP != mState) {
                // but we still consume it.
                return true;
            }
            // Default handling:
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // This is the entry point for drag slide view on touch mode.
                    mStartPointerId = event.getPointerId(0);
                    if (mViewStartRawX == null) {
                        mViewStartRawX = v.getLeft();
                    }
                    Logging.d("ACTION_DOWN, and start x = " + mViewStartRawX);
                    //Init VelocityTracker if necessary.
                    if(mVelocityTracker == null) {
                        // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                        mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        // Reset the velocity tracker back to its initial state.
                        mVelocityTracker.clear();
                    }
                    // Add a user's movement to the tracker.
                    mVelocityTracker.addMovement(event);

                    break;
                case MotionEvent.ACTION_MOVE:
                    // Track the slide icon's velocity when we move it.
                    mVelocityTracker.addMovement(event);
                    /**When you want to determine the velocity, call
                     * computeCurrentVelocity(). Then call getXVelocity()
                     * and getYVelocity() to retrieve the velocity for each pointer ID.
                     */
                    mVelocityTracker.computeCurrentVelocity(1000, 2000);
                    // Log velocity of pixels per second
                    mCurrentXVelocity = (int) VelocityTrackerCompat.getXVelocity(mVelocityTracker, mStartPointerId);

                    // Move the slide icon and post re-draw event to main thread.
                    int translateValue = (int)(event.getX() - mViewStartRawX);
                    Logging.d("ACTION_MOVE, translateValue = " + translateValue);
                    v.offsetLeftAndRight(translateValue);

                    // After we move the slide icon view, we need to post a re-draw event to let the SlideView refresh.
                    postInvalidate();

                    break;
                case MotionEvent.ACTION_UP:
                    // recycle velocity tracker.
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                    // get pointer where we start to scroll.
                    mScrollStartX = v.getLeft() - mViewStartRawX;
                    Logging.d("ACTION_UP, NOW = " + mScrollStartX);
                    // construct the scroller instance to provide help when we set view position in scroll period.
                    mScroller = new Scroller(getContext());
                    if (mScrollStartX >= getMeasuredWidth()/2) {
                        Logging.d("ACTION_UP, reveal the view.");
                        mScroller.startScroll(mScrollStartX, 0, getRight() - mScrollStartX, 0, DURATION_TIME);
                        mState = State.RUNNING;
                        postInvalidate();
                    } else {
                        Logging.d("ACTION_UP, cover the view, current velocity=" + mCurrentXVelocity +
                                ", and start scroll from =" + mScrollStartX);
                        mScroller.fling(mScrollStartX, 0, mCurrentXVelocity, 0, 0, mScrollStartX + 40, 0, 0);
                        mState = State.Fling;
                        postInvalidate();
                    }
                    break;
            }
            return true;
        }
    };

    public SlideButtonLayout(Context context) {
        this(context, null);
    }

    public SlideButtonLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    /**
     * So this will be called during parse layout xml.
     * @param child
     * @param index
     * @param params
     */
    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        // bind mSlideIcon view.
        if (mSlideIcon == null && child.getId() == R.id.slide_icon) {
            mSlideIcon = child;
            mSlideIcon.setOnTouchListener(mSlideIconOnTouchListener);
        }
    }

    /**
     * In the viewgroup, the dispatchDraw() will be triggered when invalidate the SlideView instead of onDraw()
     * @param canvas
     */
    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);
        Logging.d("draw mask");
        Bitmap bitmap = getMaskBitmap();
        drawMask(canvas, bitmap);
    }

    /**
     * Return the drawing cache of last index view in this SlideView layout
     * @return
     */
    private Bitmap getMaskBitmap() {
        int lastChildIndex = 0;
        View realView = getChildAt(lastChildIndex);
        if (realView.getDrawingCache() == null) {
            realView.setDrawingCacheEnabled(true);
            realView.buildDrawingCache();
        }
        return realView.getDrawingCache();
    }

    /**
     * Draw the arrow and reveal picture for the mask.
     * @param canvas
     * @param bitmap
     */
    private void drawMask(Canvas canvas, Bitmap bitmap) {
        Paint mPaint = new Paint();
        mPaint.setColor(0xFFFFFFFF);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));

        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(mSlideIcon.getLeft(), mSlideIcon.getTop());
        path.lineTo(mSlideIcon.getRight(), mSlideIcon.getBottom() / 2);
        path.lineTo(mSlideIcon.getLeft(), mSlideIcon.getBottom());
        path.lineTo(0, mSlideIcon.getBottom());
        path.lineTo(0, 0);

        canvas.drawPath(path, mPaint);
    }

    @Override
    public void computeScroll() {
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) {
                Logging.d("mState = "+ mState.toString() + ", mScroller.getFinalX() = " + mScroller.getFinalX());
                Logging.d("getOffset = " + (mScroller.getCurrX() - mSlideIcon.getLeft()));
                mSlideIcon.offsetLeftAndRight(mScroller.getCurrX() - mSlideIcon.getLeft());
                postInvalidate();
            } else if (State.Fling == mState) {
                Logging.d("start to scroll back to origin point, start from " + mSlideIcon.getLeft());
                mState = State.RUNNING;
                mScroller.startScroll(mSlideIcon.getLeft(), 0, -mSlideIcon.getLeft(), 0, DURATION_TIME);
                postInvalidate();
            } else if (State.RUNNING == mState) {
                mState = State.STOP;
            }
        }
    }

    /**
     * Which state the slide icon currently is.
     */
    enum State {
        STOP, // The scroll operation of slide icon has been done.
        Fling, // slide icon will first fling a little when we request to restore it.
        RUNNING; // scroll the slide icon with same speed.
    }
}
