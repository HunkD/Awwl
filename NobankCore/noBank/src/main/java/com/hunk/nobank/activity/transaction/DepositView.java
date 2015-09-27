package com.hunk.nobank.activity.transaction;

import com.hunk.nobank.activity.TransactionListFragment;
import com.hunk.nobank.contract.TransactionFields;

/**
 *
 */
public class DepositView extends TransactionListFragment.TransactionListAdapter.ViewTransactionFields {
    public DepositView(ViewTransactionType type, TransactionFields raw) {
        super(type, raw);
    }
}
