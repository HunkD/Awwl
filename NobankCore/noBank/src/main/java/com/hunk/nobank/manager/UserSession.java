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

    private boolean mLoginSuccess;
    private Map<AccountType, AccountDataManager> accountDataManagerMap;
    private LoginStateEnum mLoginState;
    private TransactionDataManager mTransactionDataManager;

    public UserSession() {
        this.accountDataManagerMap = new HashMap<>();
    }

    public void generateAccountDataManager(AccountSummary accountSummary) {
        List<AccountModel> allAccountIds = accountSummary.Accounts;
        for (AccountModel accountModel : allAccountIds) {
            accountDataManagerMap.put(accountModel.Type, new AccountDataManager(accountModel));
        }
    }

    public AccountDataManager getAccountDataManagerByType(AccountType type) {
        return accountDataManagerMap.get(type);
    }

    /**
     * Whether user has bypass login
     * @param currentUserSession
     * @return
     */
    public static boolean isPostLogin(UserSession currentUserSession) {
        return currentUserSession != null &&
                currentUserSession.getLoginState() == LoginStateEnum.Logined;
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
}
