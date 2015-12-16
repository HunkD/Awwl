package com.hunk.nobank.extension.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.hunk.nobank.R;

public class TestDialog extends Dialog {

    private final TextView mMsgView;

    public TestDialog(Context context) {
        super(context);
        setContentView(R.layout.custom_dialog);
        mMsgView = (TextView) findViewById(R.id.custom_dialog_msg);
    }

    public TextView getMsgView() {
        return mMsgView;
    }
}
