package com.hunk.test.nobank.activity.login;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.activity.login.LoginPagePresenter;
import com.hunk.nobank.activity.login.LoginView;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.LoginResp;
import com.hunk.nobank.contract.type.LoginStateEnum;
import com.hunk.nobank.extension.network.ServerError;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.UserSession;
import com.hunk.nobank.network.ErrorCode;
import com.hunk.test.utils.TestNoBankApplication;
import com.hunk.test.utils.mock.MockCore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import rx.Observable;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author HunkDeng
 * @since 2016/5/22
 */
@RunWith(RobolectricTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class LoginPagePresenterTest {

    private static final String FAKE_USER_NAME = "FAKE_USER_NAME";
    private static final String FAKE_USER_PSD = "FAKE_USER_PSD";
    private static final Boolean FAKE_IS_REMEMBER_ME = Boolean.TRUE;

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
        when(mMockUM.fetchLogin(anyString(), anyString(), anyBoolean()))
                .thenReturn(Observable.<LoginResp>error(new ServerError(ErrorCode.GENERIC_ERROR_CODE)));
        presenter.loginAction();
        verify(mMockView, times(1)).showLoading();
        verify(mMockUM, times(1)).fetchLogin(anyString(), anyString(), anyBoolean());
    }

    @Test
    public void testLoginActionSuccess() {
        // mock
        UserSession userSession = new UserSession();
        userSession.setLoginState(LoginStateEnum.Logined);
        when(mMockUM.getCurrentUserSession()).thenReturn(userSession);
        when(mMockView.getUserName()).thenReturn(FAKE_USER_NAME);
        when(mMockView.getPsd()).thenReturn(FAKE_USER_PSD);
        when(mMockView.isCheckedRememberMe()).thenReturn(FAKE_IS_REMEMBER_ME);

        LoginResp resp = new LoginResp();
        resp.loginState = LoginStateEnum.Logined;
        when(mMockUM.fetchLogin(eq(FAKE_USER_NAME), eq(FAKE_USER_PSD), eq(FAKE_IS_REMEMBER_ME)))
                .thenReturn(Observable.just(resp));
        when(mMockUM.fetchAccountSummary())
                .thenReturn(Observable.just(new AccountSummary()));
        // execute test
        LoginPagePresenter presenter = getTestObj();
        presenter.loginAction();
        // verify
        verify(mMockUM, times(1)).fetchAccountSummary();
        verify(mMockView, times(1)).dismissLoading();
        verify(mMockView, times(1)).navigateToDashboard();
    }

    @Test
    public void testLoginActionWithFailedLoginCallback() {
        // mock
        UserSession userSession = new UserSession();
        userSession.setLoginState(LoginStateEnum.Logined);
        when(mMockUM.getCurrentUserSession()).thenReturn(userSession);
        when(mMockView.getUserName()).thenReturn(FAKE_USER_NAME);
        when(mMockView.getPsd()).thenReturn(FAKE_USER_PSD);
        when(mMockView.isCheckedRememberMe()).thenReturn(FAKE_IS_REMEMBER_ME);
        when(mMockUM.fetchLogin(eq(FAKE_USER_NAME), eq(FAKE_USER_PSD), eq(FAKE_IS_REMEMBER_ME)))
                .thenReturn(Observable.<LoginResp>error(new ServerError(ErrorCode.GENERIC_ERROR_CODE)));
        // execute test
        LoginPagePresenter presenter = getTestObj();
        presenter.loginAction();
        // verify
        verify(mMockView, times(1)).dismissLoading();
        verify(mMockView, times(1)).showError(any(Throwable.class));
    }

    @Test
    public void testLoginActionWithFailedAccountSummaryCallback() {
        // mock
        UserSession userSession = new UserSession();
        userSession.setLoginState(LoginStateEnum.Logined);
        when(mMockUM.getCurrentUserSession()).thenReturn(userSession);

        when(mMockView.getUserName()).thenReturn(FAKE_USER_NAME);
        when(mMockView.getPsd()).thenReturn(FAKE_USER_PSD);
        when(mMockView.isCheckedRememberMe()).thenReturn(FAKE_IS_REMEMBER_ME);

        LoginResp resp = new LoginResp();
        resp.loginState = LoginStateEnum.Logined;
        when(mMockUM.fetchLogin(eq(FAKE_USER_NAME), eq(FAKE_USER_PSD), eq(FAKE_IS_REMEMBER_ME)))
                .thenReturn(Observable.just(resp));
        when(mMockUM.fetchAccountSummary())
                .thenReturn(Observable.<AccountSummary>error(new ServerError(ErrorCode.GENERIC_ERROR_CODE)));
        // execute test
        LoginPagePresenter presenter = getTestObj();
        presenter.loginAction();

        // verify
        verify(mMockView, times(1)).dismissLoading();
        verify(mMockView, times(1)).showError(any(Throwable.class));
        verify(mMockUM, times(1)).setCurrentUserSession(null);
    }

    @Test
    public void testOnResumeWithRememberMe() {
        // mock
        String expectedUsername = "1222";
        when(mMockUM.isRememberMe()).thenReturn(Observable.just(Boolean.TRUE));
        when(mMockUM.getRememberMeUserName()).thenReturn(Observable.just(expectedUsername));
        // execute
        LoginPagePresenter presenter = getTestObj();
        presenter.onResume();
        // verify
        verify(mMockView, times(1)).showRememberedUserName(expectedUsername);
    }

    @Test
    public void testOnResumeWithoutRememberMe() {
        // mock
        when(mMockUM.isRememberMe()).thenReturn(Observable.just(Boolean.FALSE));
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
