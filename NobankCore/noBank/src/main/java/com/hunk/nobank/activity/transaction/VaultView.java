package com.hunk.nobank.activity.transaction;

import com.hunk.nobank.activity.TransactionListFragment;
import com.hunk.nobank.contract.TransactionFields;

/**
 *
 */
public class VaultView extends TransactionListFragment.TransactionListAdapter.ViewTransactionFields {
    public VaultView(ViewTransactionType type, TransactionFields raw) {
        super(type, raw);
    }
}
