package com.hunk.nobank.activity.dashboard.transaction.view;

import com.hunk.nobank.R;
import com.hunk.nobank.activity.dashboard.transaction.NormalViewTransactionFields;
import com.hunk.nobank.activity.dashboard.transaction.ViewTransactionType;
import com.hunk.nobank.contract.TransactionFields;

/**
 *
 */
public class DepositView extends NormalViewTransactionFields {
    public DepositView(ViewTransactionType type, TransactionFields raw) {
        super(type, raw);
    }

    @Override
    public int getIconSource() {
        return R.drawable.ic_deposit;
    }
}
