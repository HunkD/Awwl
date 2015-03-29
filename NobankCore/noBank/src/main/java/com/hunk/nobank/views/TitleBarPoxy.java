package com.hunk.nobank.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunk.nobank.R;

public class TitleBarPoxy {
    private ImageView mLeftIcon;
    private ImageView mRightIcon;
    private TextView mTitle;

    public TitleBarPoxy(View titleView) {
        mLeftIcon = (ImageView) titleView.findViewById(R.id.title_bar_left_btn);
        mRightIcon = (ImageView) titleView.findViewById(R.id.title_bar_right_btn);
        mTitle = (TextView) titleView.findViewById(R.id.title_bar_title_txt);
    }

    public ImageView getLeftIcon() {
        return mLeftIcon;
    }

    public ImageView getRightIcon() {
        return mRightIcon;
    }

    public TextView getTitle() {
        return mTitle;
    }
}
