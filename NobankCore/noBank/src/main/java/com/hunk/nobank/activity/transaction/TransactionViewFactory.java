package com.hunk.nobank.activity.transaction;

import com.hunk.nobank.activity.TransactionListFragment.TransactionListAdapter.*;
import com.hunk.nobank.contract.TransactionFields;

/**
 *
 */
public class TransactionViewFactory {
    public static ViewTransactionFields getViewTransactionFields(ViewTransactionType type, TransactionFields raw) {
        ViewTransactionFields result = null;
        switch (type) {
            case DATE:
                result = new DateView(type, raw);
                break;
            case MORE:
                result = new MoreView(type, raw);
                break;
            case PAY:
                result = new PayView(type, raw);
                break;
            case DEPOSIT:
                result = new DepositView(type, raw);
                break;
            case VAULT:
                result = new VaultView(type, raw);
                break;
        }
        return result;
    }
}
