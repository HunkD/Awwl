package com.hunk.nobank.contract;

public class TransactionFields {

    private String mTitle;
    private double mMoney;
    private TransactionType mVault;
    private long mTimestamp;
    private TransactionCategory mCategory;
    private String mObject;

    public TransactionFields() {
    }

    public TransactionFields(String title, double money, TransactionType vault, long timestamp) {
        this(title, money, vault, timestamp, TransactionCategory.Debit);
    }

    public TransactionFields(String title, double money, TransactionType vault, long timestamp, TransactionCategory category) {
        this(title, money, vault, timestamp, category, null);
    }

    public TransactionFields(String title, double money, TransactionType vault, long timestamp, TransactionCategory category, String object) {
        this.mTitle = title;
        this.mMoney = money;
        this.mVault = vault;
        this.mTimestamp = timestamp;
        this.mCategory = category;
        this.mObject = object;
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

    public long getTimeStamp() {
        return mTimestamp;
    }

    public TransactionCategory getCategory() {
        return mCategory;
    }

    public String getObject() {
        return mObject;
    }
}
