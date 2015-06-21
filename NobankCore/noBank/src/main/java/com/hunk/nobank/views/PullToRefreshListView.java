package com.hunk.nobank.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hunk.nobank.R;
import com.hunk.nobank.util.ViewHelper;

public class PullToRefreshListView extends ListView {

    private RelativeLayout mHeader;
    private int mFistItem;

    private PullTouchEventState mTouchEventState = new PullTouchEventState();
    private float mStartY;
    private int mHeaderHeight;
    private int mScrollState;
    private State mState;

    // Constructors

    public PullToRefreshListView(Context context) {
        this(context, null);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI();
    }

    // private methods

    private void setupUI() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mHeader = (RelativeLayout) inflater.inflate(R.layout.item_transaction_header, null);
        if (!isInEditMode()) {
            ViewHelper.updateFontsStyle(mHeader);
        }
        // hide mHeader first
        hideHeaderView();
        //
        setUpHeaderUI(mHeader);
        //
        addHeaderView(mHeader);
        this.setOnScrollListener(new MyOnScrollListener());
    }

    private TextView mHeaderTitle;
    private TextView mLastUpdateTimestamp;

    private void setUpHeaderUI(View header) {
        mHeaderTitle = (TextView) header.findViewById(R.id.pull_to_refresh_txt);
        mLastUpdateTimestamp = (TextView) header.findViewById(R.id.last_refresh_timestamp);
    }

    private void hideHeaderView() {
        if (mHeaderHeight == 0) {
            mHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver obs = mHeader.getViewTreeObserver();

                    mHeaderHeight = mHeader.getMeasuredHeight();

                    setHeaderHeight(0);
                    setHeaderPadding(-mHeaderHeight);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this);
                    } else {
                        obs.removeGlobalOnLayoutListener(this);
                    }
                }

            });
        } else {
            setHeaderHeight(0);
            setHeaderPadding(-mHeaderHeight);
        }
    }

    private void showHeaderView() {
        setHeaderPadding(0);
        setHeaderHeight(mHeaderHeight);
    }

    private void setHeaderPadding(/*@DimenRes*/ int topPadding) {
        mHeader.setPadding(mHeader.getPaddingLeft(), topPadding, mHeader.getPaddingRight(), mHeader.getPaddingBottom());
    }

    private void setHeaderHeight(int height) {
        ViewGroup.LayoutParams lp = mHeader.getLayoutParams();
        lp.height = height;
        mHeader.setLayoutParams(lp);
    }

    private void setHeaderText(State state) {
        switch (state) {
            case Pull:
                mHeaderTitle.setText(R.string.pull_to_refresh);
                break;
            case Refresh:
                mHeaderTitle.setText(R.string.release_to_fetch);
                break;
        }
    }

    // Inner classes

    private class MyOnScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            PullToRefreshListView.this.mScrollState = scrollState;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            PullToRefreshListView.this.mFistItem = firstVisibleItem;
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        this.mTouchEventState.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    private class PullTouchEventState {

        private boolean mIsPulling;

        public void onTouchEvent(MotionEvent ev) {

            int action = MotionEventCompat.getActionMasked(ev);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (mFistItem == 0 && mState != State.Refresh) {
                        mIsPulling = true;
                        PullToRefreshListView.this.mStartY = ev.getY();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!mIsPulling) { // in case when we reset head, moving event has been triggered.
                        return;
                    }
                    int space = (int) (ev.getY() - mStartY);
                    // show Header
                    int topPadding = space - mHeaderHeight;
                    if (topPadding <= 0) {
                        setHeaderPadding(topPadding);
                    } else {
                        setHeaderPadding(0);
                    }
                    setHeaderHeight(space);

                    if (space > 0 && space < mHeaderHeight) {
                        mState = State.Pull;
                        setHeaderText(State.Pull);
                    } else if (space > mHeaderHeight + 30 /* && mScrollState = scroll // why we need this flag? */) {
                        mState = State.Refresh;
                        setHeaderText(State.Refresh);
                    } else if (space < 0) {
                        mState = State.Pull;
                        setHeaderText(State.Pull);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mIsPulling) { // in case when we reset head, moving event has been triggered.
                        return;
                    } else {
                        switch (mState) {
                            case Pull:
                                hideHeaderView();
                                break;
                            case Refresh:
                                showHeaderView();
                                break;
                        }
                    }
                    mIsPulling = false;
            }
        }
    }

    private enum State {
        Refresh, None, Pull
    }
}
