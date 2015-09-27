package com.hunk.nobank.activity.transaction;

/**
 *
 */
public enum ViewTransactionType {
    DATE(1), MORE(2), PAY(3), VAULT(4), DEPOSIT(5);
    public final int value;
    ViewTransactionType(int value) {
        this.value = value;
    }
}
