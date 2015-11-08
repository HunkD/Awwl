package com.hunk.nobank.extension.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hunk.nobank.R;

/**
 *
 */
public class PersonalInfoItemView extends FrameLayout {
    private final String mLabelTitle;
    private final String mLabelDetail;
    private TextView mLabelTitleView;
    private TextView mLabelDetailView;
    private Button mVerifyBtn;

    public PersonalInfoItemView(Context context) {
        this(context, null);
    }

    public PersonalInfoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PersonalInfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.item_personal_info, this, true);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PersonalInfoItemView,
                0, 0);
        try {
            mLabelTitle = a.getString(R.styleable.PersonalInfoItemView_piilabelTitle);
            mLabelDetail = a.getString(R.styleable.PersonalInfoItemView_piilabelDetail);
        } finally {
            a.recycle();
        }
    }

    private void setValue() {
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
        mLabelTitleView = (TextView) this.findViewById(R.id.label_title);
        mLabelDetailView = (TextView) this.findViewById(R.id.label_detail);
        mVerifyBtn = (Button)this.findViewById(R.id.label_verify);
        setValue();
    }

    public Button getVerifyBtn() {
        return mVerifyBtn;
    }
}
