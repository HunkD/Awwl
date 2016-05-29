package com.hunk.test.nobank.activity.login;

import android.content.Intent;
import android.widget.TextView;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.activity.RootActivity;
import com.hunk.nobank.activity.login.LoginPageActivity;
import com.hunk.nobank.contract.type.LoginStateEnum;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.UserSession;
import com.hunk.test.utils.mock.MockCore;
import com.hunk.test.utils.NBAbstractTest;
import com.hunk.test.utils.NetworkHandlerStub;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ReflectionHelpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class LoginPageActivityTest extends NBAbstractTest {

    private NetworkHandlerStub mNetworkHandlerStub;
    private UserManager mMockUM;

    @Before
    public void prepare() {
        // set next response
        mNetworkHandlerStub =
                (NetworkHandlerStub) Core.getInstance().getNetworkHandler();

        mNetworkHandlerStub.clear();

        mMockUM = MockCore.mockUserManager();
        when(mMockUM.isRememberMe()).thenReturn(false);
    }

    @Test
    public void testNavigateToDashboard() {
        // mock user session is logined
        UserSession userSession = new UserSession();
        userSession.setLoginState(LoginStateEnum.Logined);
        when(mMockUM.getCurrentUserSession()).thenReturn(userSession);
        // execute test
        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);
        ShadowActivity shadowActivity = shadowOf(activity);

        activity.navigateToDashboard();
        // expected
        Intent gotoRootActivity = new Intent();
        gotoRootActivity.setPackage(RuntimeEnvironment.application.getPackageName());
        gotoRootActivity.setAction(RootActivity.ACTION);
        gotoRootActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // verify
        assertTrue(gotoRootActivity.filterEquals(shadowActivity.getNextStartedActivity()));
    }

    @Test
    public void testClearPassword() {
        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);
        TextView psdTextView = ReflectionHelpers.getField(activity, "mInputLoginPsd");
        // set fake data
        psdTextView.setText("passwordStr");
        // execute test
        activity.clearPassword();
        // verify
        assertEquals("", psdTextView.getText().toString());
    }

    @Test
    public void showRememberedUserName() {
        // prepare
        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);
        // fake data
        String expectedUsername = "ddddd";
        // execute test
        activity.showRememberedUserName(expectedUsername);
        // verify
        assertEquals(expectedUsername, activity.getUserName());
    }

    @Test
    public void testIsCheckedRememberMe() {
        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);
        assertFalse(activity.isCheckedRememberMe());
    }

    @Test
    public void testGetUserName() {
        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);
        assertTrue("".equals(activity.getUserName()));
    }

    @Test
    public void testGetPsd() {
        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);
        assertTrue("".equals(activity.getPsd()));
    }
}
