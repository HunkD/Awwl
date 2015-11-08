package com.hunk.nobank.extension.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunk.nobank.R;

/**
 *
 */
public class IconLabelDetailView extends FrameLayout {
    private final int mLabelIcon;
    private final String mLabelTitle;
    private final String mLabelDetail;
    private ImageView mLabelIconView;
    private TextView mLabelTitleView;
    private TextView mLabelDetailView;

    public IconLabelDetailView(Context context) {
        this(context, null);
    }

    public IconLabelDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconLabelDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.item_icon_label_detail, this, true);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.IconLabelDetailView,
                0, 0);
        try {
            mLabelIcon = a.getResourceId(R.styleable.IconLabelDetailView_labelIcon, 0);
            mLabelTitle = a.getString(R.styleable.IconLabelDetailView_labelTitle);
            mLabelDetail = a.getString(R.styleable.IconLabelDetailView_labelDetail);
        } finally {
            a.recycle();
        }
    }

    private void setValue() {
        mLabelIconView.setImageResource(mLabelIcon);
        mLabelTitleView.setText(mLabelTitle);
        mLabelDetailView.setText(mLabelDetail);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLabelIconView = (ImageView) this.findViewById(R.id.label_icon);
        mLabelTitleView = (TextView) this.findViewById(R.id.label_title);
        mLabelDetailView = (TextView) this.findViewById(R.id.label_detail);
        setValue();
    }
}
