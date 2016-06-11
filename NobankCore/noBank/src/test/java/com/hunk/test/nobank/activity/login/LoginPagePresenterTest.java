package com.hunk.test.nobank.activity.login;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.activity.login.LoginPagePresenter;
import com.hunk.nobank.activity.login.LoginView;
import com.hunk.nobank.contract.type.LoginStateEnum;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.UserSession;
import com.hunk.nobank.manager.dataBasic.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.nobank.model.LoginReqPackage;
import com.hunk.test.utils.mock.MockCore;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author HunkDeng
 * @since 2016/5/22
 */
@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class LoginPagePresenterTest {

    private LoginView mMockView;
    private UserManager mMockUM;

    @Before
    public void setup() {
        mMockView = mock(LoginView.class);
        mMockUM = MockCore.mockUserManager();
    }

    @Test
    public void testLoginActionWithInValidInput() {
        LoginPagePresenter presenter = getTestObj();
        when(mMockView.getUserName()).thenReturn("");
        when(mMockView.getPsd()).thenReturn("");
        when(mMockView.isCheckedRememberMe()).thenReturn(false);
        presenter.loginAction();
        verify(mMockView, times(1)).clearPassword();
        verify(mMockView, times(1)).showErrorMessage(anyString());
    }

    @Test
    public void testLoginActionWithValidInput() {
        LoginPagePresenter presenter = getTestObj();
        when(mMockView.getUserName()).thenReturn("1");
        when(mMockView.getPsd()).thenReturn("2");
        when(mMockView.isCheckedRememberMe()).thenReturn(false);
        presenter.loginAction();
        verify(mMockView, times(1)).showLoading();
        verify(mMockUM, times(1)).fetchLogin(any(LoginReqPackage.class), any(ViewManagerListener.class));
    }

    @Test
    public void testLoginActionWithSuccessLoginCallback() {
        // mock
        UserSession userSession = new UserSession();
        userSession.setLoginState(LoginStateEnum.Logined);
        when(mMockUM.getCurrentUserSession()).thenReturn(userSession);
        // execute test
        LoginPagePresenter presenter = getTestObj();
        ViewManagerListener viewManagerListener =
                ReflectionHelpers.getField(presenter, "mManagerListener");

        viewManagerListener.onSuccess(
                UserManager.MANAGER_ID,
                UserManager.METHOD_LOGIN,
                null);
        // verify
        verify(mMockUM, times(1)).fetchAccountSummary(any(AccountSummaryPackage.class),
                any(ViewManagerListener.class));
    }

    @Test
    public void testLoginActionWithSuccessAccountSummaryCallback() {
        // mock
        // execute test
        LoginPagePresenter presenter = getTestObj();
        ViewManagerListener viewManagerListener =
                ReflectionHelpers.getField(presenter, "mManagerListener");

        viewManagerListener.onSuccess(
                UserManager.MANAGER_ID,
                UserManager.METHOD_ACCOUNT_SUMMARY,
                null);
        // verify
        verify(mMockView, times(1)).dismissLoading();
        verify(mMockView, times(1)).navigateToDashboard();
    }

    @Test
    public void testLoginActionWithFailedLoginCallback() {
        // mock
        UserSession userSession = new UserSession();
        userSession.setLoginState(LoginStateEnum.Logined);
        when(mMockUM.getCurrentUserSession()).thenReturn(userSession);
        // execute test
        LoginPagePresenter presenter = getTestObj();
        ViewManagerListener viewManagerListener =
                ReflectionHelpers.getField(presenter, "mManagerListener");

        viewManagerListener.onFailed(
                UserManager.MANAGER_ID,
                UserManager.METHOD_LOGIN,
                null);
        // verify
        verify(mMockView, times(1)).dismissLoading();
        verify(mMockView, times(1)).showErrorMessage(anyString());
    }

    @Test
    public void testLoginActionWithFailedAccountSummaryCallback() {
        // mock
        UserSession userSession = new UserSession();
        userSession.setLoginState(LoginStateEnum.Logined);
        when(mMockUM.getCurrentUserSession()).thenReturn(userSession);
        // execute test
        LoginPagePresenter presenter = getTestObj();
        ViewManagerListener viewManagerListener =
                ReflectionHelpers.getField(presenter, "mManagerListener");

        viewManagerListener.onFailed(
                UserManager.MANAGER_ID,
                UserManager.METHOD_ACCOUNT_SUMMARY,
                null);
        // verify
        verify(mMockView, times(1)).dismissLoading();
        verify(mMockView, times(1)).showErrorMessage(anyString());
        verify(mMockUM, times(1)).setCurrentUserSession(null);
    }

    @Test
    public void testOnResumeWithRememberMe() {
        // mock
        String expectedUsername = "1222";
        when(mMockUM.isRememberMe()).thenReturn(true);
        when(mMockUM.getRememberMeUserName()).thenReturn(expectedUsername);
        // execute
        LoginPagePresenter presenter = getTestObj();
        presenter.onResume();
        // verify
        verify(mMockView, times(1)).showRememberedUserName(expectedUsername);
    }

    @Test
    public void testOnResumeWithoutRememberMe() {
        // mock
        when(mMockUM.isRememberMe()).thenReturn(false);
        // execute
        LoginPagePresenter presenter = getTestObj();
        presenter.onResume();
        // verify
        verify(mMockView, times(0)).showRememberedUserName(anyString());
    }

    @Test
    public void testOnPauseWithRememberMeCheckedInView() {
        // mock
        String expectedUsername = "1222";
        when(mMockView.isCheckedRememberMe()).thenReturn(true);
        when(mMockView.getUserName()).thenReturn(expectedUsername);
        // execute
        LoginPagePresenter presenter = getTestObj();
        presenter.onPause();
        // verify
        verify(mMockUM, times(1)).setRememberMe(true, expectedUsername);
    }

    @Test
    public void testOnPauseWithoutRememberMeCheckedInView() {
        // mock
        when(mMockView.isCheckedRememberMe()).thenReturn(false);
        // execute
        LoginPagePresenter presenter = getTestObj();
        presenter.onPause();
        // verify
        verify(mMockUM, times(1)).setRememberMe(false, null);
    }

    @Test
    public void testOnDestroy() {
        LoginPagePresenter presenter = getTestObj();
        presenter.detach();
        assertNull(ReflectionHelpers.getField(presenter, "mView"));
    }
    
    private LoginPagePresenter getTestObj() {
        LoginPagePresenter presenter = new LoginPagePresenter();
        presenter.attach(mMockView);
        return presenter;
    }
}
