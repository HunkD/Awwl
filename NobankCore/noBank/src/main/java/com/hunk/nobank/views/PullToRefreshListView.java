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

import com.hunk.abcd.extension.font.UpdateFont;
import com.hunk.nobank.R;
import com.hunk.abcd.extension.log.Logging;
import com.hunk.abcd.extension.util.ViewHelper;

@Deprecated
public class PullToRefreshListView extends ListView {

    private RelativeLayout mHeader;
    private int mFistItem;

    private PullTouchEventState mTouchEventState = new PullTouchEventState(this);
    private int mHeaderHeight;
    private int mScrollState;
    private State mState;
    private ListListener mListListener;
    private boolean mPullable = true;

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
            UpdateFont.updateFontsStyle(mHeader);
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

    public void hideHeaderView() {
        mState = State.None;
        if (mHeaderHeight == 0) {
            mHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver obs = mHeader.getViewTreeObserver();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this);
                    } else {
                        obs.removeGlobalOnLayoutListener(this);
                    }

                    mHeaderHeight = mHeader.getMeasuredHeight();

                    setHeaderHeight(0);
                    setHeaderPadding(-mHeaderHeight);
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

    private void setHeaderText() {
        switch (mState) {
            case Pull:
                mHeaderTitle.setText(R.string.pull_to_refresh);
                break;
            case Release:
                mHeaderTitle.setText(R.string.release_to_fetch);
                break;
            case Fetching:
                mHeaderTitle.setText(R.string.fetching);
                break;
        }
    }

    public void setListListener(ListListener listListener) {
        this.mListListener = listListener;
    }

    public void setPullable(boolean pullable) {
        this.mPullable = pullable;
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
            Logging.d("pull to refresh : onScroll first item = " + mFistItem);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        this.mTouchEventState.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    private class PullTouchEventState {

        private final PullToRefreshListView mListView;
        private boolean mIsActionOnFirstItem;
        private boolean mIsPulling;
        private float mStartY;

        public PullTouchEventState(PullToRefreshListView listView) {
            this.mListView = listView;
        }

        public void onTouchEvent(MotionEvent ev) {

            int action = MotionEventCompat.getActionMasked(ev);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Logging.d("pull to refresh : ACTION_DOWN first item = " + mFistItem);
                    if (mState == State.None && mPullable) {
                        if (mFistItem == 0 && mListView.getChildAt(0).getTop() == 0) {
                            mIsActionOnFirstItem = true;
                            mStartY = ev.getY();
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!mIsActionOnFirstItem) { // in case when we reset head, moving event has been triggered.
                        return;
                    }
                    // If currently the visible first item is 0, and the move action is pulling down,
                    // Then we can make mIsPulling flag = true.
                    int space = (int) (ev.getY() - mStartY);
                    if (space > 0 && !mIsPulling) {
                        mIsPulling = true;
                    }
                    // If user pulling down first, then pulling up. ListView will move up, refresh
                    // header will reduce height at the same time. This isn't correct, we just need
                    // reduce height operation, so setSelection = 0 all the time.
                    if (mIsPulling) {
                        mListView.setSelection(0);
                    } else {
                        return;
                    }

                    Logging.d("pull to refresh : ACTION_MOVE space = " + space);
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
                        setHeaderText();
                    } else if (space > mHeaderHeight + 30 /* && mScrollState = scroll // why we need this flag? */) {
                        mState = State.Release;
                        setHeaderText();
                    } else if (space < 0) {
                        mState = State.Pull;
                        setHeaderText();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mIsPulling) { // in case when we reset head, moving event has been triggered.
                    } else {
                        switch (mState) {
                            case Pull:
                                hideHeaderView();
                                break;
                            case Release:
                                mState = State.Fetching;
                                setHeaderText();
                                showHeaderView();
                                triggerRefresh();
                                break;
                        }
                    }
                    // Reset all status flags.
                    mIsPulling = false;
                    mIsActionOnFirstItem = false;
                    mStartY = 0;
            }
        }
    }

    private void triggerRefresh() {
        if (mListListener != null) {
            mListListener.refresh();
        }
    }

    private enum State {
        Release, None, Pull, Fetching
    }

    public interface ListListener {
        void refresh();
        void more();
    }
}
