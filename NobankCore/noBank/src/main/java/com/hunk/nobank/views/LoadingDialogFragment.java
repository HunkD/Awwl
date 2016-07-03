package com.hunk.nobank.views;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hunk.abcd.extension.font.UpdateFont;
import com.hunk.nobank.R;
import com.hunk.abcd.extension.util.ViewHelper;

public class LoadingDialogFragment extends DialogFragment {
    public static LoadingDialogFragment newInstance() {
        return new LoadingDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // full screen
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_loading, container, false);
        UpdateFont.updateFontsStyle((ViewGroup) v);
        // Make App theme style transparency in setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme).
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return v;
    }

}
