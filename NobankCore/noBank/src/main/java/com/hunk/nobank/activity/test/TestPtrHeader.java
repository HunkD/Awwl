package com.hunk.nobank.activity.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.hunk.nobank.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by HunkDeng on 2017/2/19.
 */
public class TestPtrHeader extends FrameLayout implements PtrUIHandler {
    public TestPtrHeader(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.test_ptr_header, this, true);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
