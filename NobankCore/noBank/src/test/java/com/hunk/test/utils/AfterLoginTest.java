package com.hunk.test.utils;

import com.hunk.nobank.contract.type.LoginStateEnum;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.UserSession;
import com.hunk.test.utils.mock.MockCore;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author HunkDeng
 * @since 2016/5/23
 */
public abstract class AfterLoginTest extends NBAbstractTest {
    private UserManager mMockedUM;
    private UserSession mMockedUS;
    private TransactionDataManager mMockedTM;

    @Override
    public void setup() {
        super.setup();

        mMockedUM = MockCore.mockUserManager();
        mMockedUS = mock(UserSession.class);
        mMockedTM = mock(TransactionDataManager.class);
        when(mMockedUS.getLoginState()).thenReturn(LoginStateEnum.Logined);
        when(mMockedUM.getCurrentUserSession()).thenReturn(mMockedUS);
        when(mMockedUS.getTransactionDataManager()).thenReturn(mMockedTM);
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

    public TransactionDataManager getMockedTM() {
        return mMockedTM;
    }
}
