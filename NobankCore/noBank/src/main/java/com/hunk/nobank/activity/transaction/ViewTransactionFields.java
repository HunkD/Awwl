package com.hunk.nobank.activity.transaction;

import com.hunk.nobank.contract.TransactionFields;

/**
 *
 */
public abstract class ViewTransactionFields implements Rendering {
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