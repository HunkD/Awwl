package com.hunk.test.nobank.activity.welcome;

import android.support.annotation.NonNull;

import com.hunk.nobank.activity.welcome.WelcomePagePresenter;
import com.hunk.nobank.activity.welcome.WelcomeView;
import com.hunk.nobank.manager.UserManager;
import com.hunk.test.utils.NBAbstractTest;
import com.hunk.test.utils.mock.MockCore;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.util.ReflectionHelpers;

import rx.Observable;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author HunkDeng
 * @since 2016/5/22
 */
public class WelcomePagePresenterTest extends NBAbstractTest {

    private WelcomeView mMockView;
    private UserManager mMockUDM;

    @Before
    public void setup() {
        mMockView = mock(WelcomeView.class);
        mMockUDM = MockCore.mockUserManager();
        when(mMockUDM.isRememberMe()).thenReturn(Observable.just(Boolean.FALSE));
    }

    /**
     * userManager.rememberMe = true
     * presenter.checkRememberMe = true
     */
    @Test
    public void testOnResumeFirstTimeCheckRememberMe() {
        when(mMockUDM.isRememberMe()).thenReturn(Observable.just(Boolean.TRUE));
        WelcomePagePresenter presenter = getTestObj();
        presenter.onResume();
        verify(mMockView, times(1)).onClickSignIn(null);
    }

    @NonNull
    private WelcomePagePresenter getTestObj() {
        WelcomePagePresenter presenter = new WelcomePagePresenter();
        presenter.attach(mMockView);
        return presenter;
    }

    /**
     * userManager.rememberMe = false
     * presenter.checkRememberMe = true
     */
    @Test
    public void testOnResumeFirstTimeCheckRememberMe2() {
        when(mMockUDM.isRememberMe()).thenReturn(Observable.just(Boolean.FALSE));
        WelcomePagePresenter presenter = getTestObj();
        presenter.onResume();
        verify(mMockView, times(0)).onClickSignIn(null);
    }

    /**
     * userManager.rememberMe = true
     * presenter.checkRememberMe = false
     */
    @Test
    public void testOnResumeSecondTimeCheckRememberMe() {
        when(mMockUDM.isRememberMe()).thenReturn(Observable.just(Boolean.TRUE));
        WelcomePagePresenter presenter = getTestObj();
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
        when(mMockUDM.isRememberMe()).thenReturn(Observable.just(Boolean.FALSE));
        WelcomePagePresenter presenter = getTestObj();
        ReflectionHelpers.setField(presenter, "mCheckRememberMe", false);
        presenter.onResume();
        verify(mMockView, times(0)).onClickSignIn(null);
    }

    @Test
    public void testOnDestroy() {
        WelcomePagePresenter presenter = getTestObj();
        presenter.detach();
        assertNull(ReflectionHelpers.getField(presenter, "mView"));
    }
}
