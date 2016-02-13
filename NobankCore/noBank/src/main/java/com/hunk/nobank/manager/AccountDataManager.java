package com.hunk.nobank.manager;

import com.hunk.nobank.contract.AccountModel;
import com.hunk.nobank.manager.dataBasic.DataManager;

/**
 *
 */
public class AccountDataManager extends DataManager {

    public AccountModel getAccountModel() {
        return accountModel;
    }

    private final AccountModel accountModel;

    public AccountDataManager(AccountModel accountModel) {
        this.accountModel = accountModel;
    }

    @Override
    public String getManagerId() {
        return accountModel.Type.toString();
    }
}
