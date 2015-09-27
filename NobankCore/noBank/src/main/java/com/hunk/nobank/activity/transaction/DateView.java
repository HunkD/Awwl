package com.hunk.nobank.activity.transaction;

import com.hunk.nobank.activity.TransactionListFragment;
import com.hunk.nobank.contract.TransactionFields;

/**
 *
 */
public class DateView extends TransactionListFragment.TransactionListAdapter.ViewTransactionFields {
    public DateView(ViewTransactionType type, TransactionFields raw) {
        super(type, raw);
    }
}
