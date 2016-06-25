package com.hunk.nobank.contract;

public class TransactionFields {

    private String mTitle;
    private double mMoney;
    private TransactionType mVault;
    private long mTimestamp;
    private TransactionCategory mCategory;
    private String mObject;
    private String imageId;

    public TransactionFields() {
    }

    public TransactionFields(String title, double money, TransactionType vault, long timestamp) {
        this(title, money, vault, timestamp, TransactionCategory.Debit);
    }

    public TransactionFields(String title, double money, TransactionType vault, long timestamp, TransactionCategory category) {
        this(title, money, vault, timestamp, category, null);
    }

    public TransactionFields(String title, double money, TransactionType vault, long timestamp, TransactionCategory category, String object) {
        this(title, money, vault, timestamp, category, object, null);
    }

    public TransactionFields(String mTitle, double mMoney, TransactionType mVault, long mTimestamp, TransactionCategory mCategory, String mObject, String imageId) {
        this.mTitle = mTitle;
        this.mMoney = mMoney;
        this.mVault = mVault;
        this.mTimestamp = mTimestamp;
        this.mCategory = mCategory;
        this.mObject = mObject;
        this.imageId = imageId;
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

    public String getImageId() {
        return imageId;
    }
}
