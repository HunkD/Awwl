package com.hunk.nobank.activity.dashboard.transaction;

/**
 *
 */
public enum ViewTransactionType {
    DATE(0), MORE(1), PAY(2), VAULT(3), DEPOSIT(4);
    public final int value;
    ViewTransactionType(int value) {
        this.value = value;
    }
}
