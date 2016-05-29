package com.hunk.test.nobank.activity.welcome;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.activity.welcome.WelcomePagePresenter;
import com.hunk.nobank.activity.welcome.WelcomeView;
import com.hunk.nobank.manager.UserManager;
import com.hunk.test.utils.mock.MockCore;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import static org.junit.Assert.assertNull;
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
public class WelcomePagePresenterTest {

    private WelcomeView mMockView;
    private UserManager mMockUDM;

    @Before
    public void setup() {
        mMockView = mock(WelcomeView.class);
        mMockUDM = MockCore.mockUserManager();
        when(mMockUDM.isRememberMe()).thenReturn(false);
    }

    /**
     * userManager.rememberMe = true
     * presenter.checkRememberMe = true
     */
    @Test
    public void testOnResumeFirstTimeCheckRememberMe() {
        when(mMockUDM.isRememberMe()).thenReturn(true);
        WelcomePagePresenter presenter = new WelcomePagePresenter(mMockView);
        presenter.onResume();
        verify(mMockView, times(1)).onClickSignIn(null);
    }

    /**
     * userManager.rememberMe = false
     * presenter.checkRememberMe = true
     */
    @Test
    public void testOnResumeFirstTimeCheckRememberMe2() {
        when(mMockUDM.isRememberMe()).thenReturn(false);
        WelcomePagePresenter presenter = new WelcomePagePresenter(mMockView);
        presenter.onResume();
        verify(mMockView, times(0)).onClickSignIn(null);
    }

    /**
     * userManager.rememberMe = true
     * presenter.checkRememberMe = false
     */
    @Test
    public void testOnResumeSecondTimeCheckRememberMe() {
        when(mMockUDM.isRememberMe()).thenReturn(true);
        WelcomePagePresenter presenter = new WelcomePagePresenter(mMockView);
        ReflectionHelpers.setField(presenter, "mCheckRememberMe", false);
        presenter.onResume();
        verify(mMockView, times(0)).onClickSignIn(null);
    }

    /**
     * userManager.rememberMe = false
     * presenter.checkRememberMe = false
     */
    @Test
    public void testOnResumeSecondTimeCheckRememberMe2() {
        when(mMockUDM.isRememberMe()).thenReturn(false);
        WelcomePagePresenter presenter = new WelcomePagePresenter(mMockView);
        ReflectionHelpers.setField(presenter, "mCheckRememberMe", false);
        presenter.onResume();
        verify(mMockView, times(0)).onClickSignIn(null);
    }

    @Test
    public void testOnDestroy() {
        WelcomePagePresenter presenter = new WelcomePagePresenter(mMockView);
        presenter.onDestroy();
        assertNull(ReflectionHelpers.getField(presenter, "mView"));
    }
}
