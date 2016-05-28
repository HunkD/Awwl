package com.hunk.nobank.activity.dashboard.transaction.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hunk.nobank.activity.dashboard.transaction.ViewTransactionFields;
import com.hunk.nobank.activity.dashboard.transaction.ViewTransactionType;
import com.hunk.nobank.contract.TransactionFields;

/**
 *
 */
public class DateView extends ViewTransactionFields {
    public DateView(ViewTransactionType type, TransactionFields raw) {
        super(type, raw);
    }

    @Override
    public View render(Context context, int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
