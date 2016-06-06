package com.hunk.nobank.manager;

import com.hunk.nobank.contract.AccountModel;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.AccountType;
import com.hunk.nobank.contract.type.LoginStateEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A user session will hold every memory cache.
 * Once we logout we can easy to clear this object by recreate it.
 * @author HunkDeng
 * @since 2016/5/2
 */
public class UserSession {

    private LoginStateEnum mLoginState;
    /**
     * Transaction data related
     */
    private TransactionDataManager mTransactionDataManager;
    /**
     * Account data related
     */
    private AccountDataManager mAccountDataManager;
    /**
     * Vault data related
     */
    private VaultDataManager mVaultDataManager;

    public UserSession() {
    }

    public void generateAccountDataManager(AccountSummary accountSummary) {
        List<AccountModel> allAccountIds = accountSummary.Accounts;
        for (AccountModel accountModel : allAccountIds) {
            if (AccountType.Main == accountModel.Type) {
                mAccountDataManager = new AccountDataManager(accountModel);
            } else if (AccountType.Vault == accountModel.Type) {
                mVaultDataManager = new VaultDataManager(accountModel);
            }
        }
    }

    public LoginStateEnum getLoginState() {
        return mLoginState;
    }

    public void setLoginState(LoginStateEnum loginState) {
        this.mLoginState = loginState;
    }

    public void generateTransactionDataManager(AccountSummary accountSummary) {
        mTransactionDataManager = new TransactionDataManager(accountSummary);
    }

    public TransactionDataManager getTransactionDataManager() {
        return mTransactionDataManager;
    }

    public VaultDataManager getVaultDataManager() {
        return mVaultDataManager;
    }

    public AccountDataManager getAccountDataManager() {
        return mAccountDataManager;
    }
}
