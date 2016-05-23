package com.hunk.nobank.activity.dashboard.transaction;

import android.view.View;

import com.hunk.nobank.contract.TransactionFields;

/**
 *
 */
public abstract class ViewTransactionFields implements Rendering, View.OnClickListener {
    ViewTransactionType mViewType;
    TransactionFields mTransactionFields;

    public ViewTransactionFields(ViewTransactionType viewType, TransactionFields transactionFields) {
        this.mViewType = viewType;
        this.mTransactionFields = transactionFields;
    }

    public TransactionFields getTransactionFields() {
        return mTransactionFields;
    }

    public ViewTransactionType getViewType() {
        return mViewType;
    }
}