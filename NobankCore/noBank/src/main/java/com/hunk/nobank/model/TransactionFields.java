package com.hunk.nobank.model;

public class TransactionFields {

    private final String mTitle;
    private final double mMoney;
    private final TransactionType mVault;

    public TransactionFields(String title, double money, TransactionType vault) {
        this.mTitle = title;
        this.mMoney = money;
        this.mVault = vault;
    }

    public String getTitle() {
        return mTitle;
    }

    public double getMoney() {
        return mMoney;
    }

    public TransactionType getVault() {
        return mVault;
    }
}
