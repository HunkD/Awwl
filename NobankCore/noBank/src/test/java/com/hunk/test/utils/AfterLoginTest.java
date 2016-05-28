package com.hunk.test.utils;

import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.type.LoginStateEnum;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.UserSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author HunkDeng
 * @since 2016/5/23
 */
public abstract class AfterLoginTest extends NBAbstractTest {
    private UserManager mMockedUM;
    private UserSession mMockedUS;

    @Override
    public void setup() {
        super.setup();

        mMockedUM = MockCore.mockUserManager();
        mMockedUS = mock(UserSession.class);
        when(mMockedUS.getLoginState()).thenReturn(LoginStateEnum.Logined);
        when(mMockedUM.getCurrentUserSession()).thenReturn(mMockedUS);
        when(mMockedUS.getTransactionDataManager()).thenReturn(new TransactionDataManager(new AccountSummary()));
    }

    public UserManager getMockedUM() {
        return mMockedUM;
    }

    /**
     * @return
     *  mocked UserSession obj
     */
    public UserSession getMockedUS() {
        return mMockedUS;
    }
}
