package com.hunk.nobank.activity.transaction;

import com.hunk.nobank.activity.TransactionListFragment;
import com.hunk.nobank.contract.TransactionFields;

/**
 *
 */
public class PayView extends TransactionListFragment.TransactionListAdapter.ViewTransactionFields {
    public PayView(ViewTransactionType type, TransactionFields raw) {
        super(type, raw);
    }
}
