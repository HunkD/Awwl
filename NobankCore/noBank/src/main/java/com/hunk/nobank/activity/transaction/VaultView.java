package com.hunk.nobank.activity.transaction;

import com.hunk.nobank.R;
import com.hunk.nobank.contract.TransactionFields;

/**
 *
 */
public class VaultView extends NormalViewTransactionFields {
    public VaultView(ViewTransactionType type, TransactionFields raw) {
        super(type, raw);
    }

    @Override
    public int getIconSource() {
        return R.drawable.ic_vault;
    }
}
