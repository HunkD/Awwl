package com.hunk.nobank.activity.transaction;

import com.hunk.nobank.activity.TransactionListFragment;
import com.hunk.nobank.contract.TransactionFields;

/**
 *
 */
public class MoreView extends TransactionListFragment.TransactionListAdapter.ViewTransactionFields {
    public MoreView(ViewTransactionType type, TransactionFields raw) {
        super(type, raw);
    }
}
