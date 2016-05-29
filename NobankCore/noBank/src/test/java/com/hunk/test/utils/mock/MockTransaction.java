package com.hunk.test.utils.mock;

import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.contract.TransactionType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HunkDeng
 * @since 2016/5/29
 */
public class MockTransaction {
    /**
     * @return
     *  a default fake transaction list
     */
    public static List<TransactionFields> fakeTransactionList() {
        List<TransactionFields> list = new ArrayList<>();
        list.add(new TransactionFields("Move to vault", 15.5, TransactionType.VAULT, 1000));
        list.add(new TransactionFields("Pay to Hunk", 19.5, TransactionType.PAY, 1000));
        list.add(new TransactionFields("Deposit from check", 25.5, TransactionType.DEPOSIT, 1000));
        return list;
    }
}
