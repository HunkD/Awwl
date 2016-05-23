package com.hunk.test.nobank.activity;

import android.widget.TextView;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.dashboard.BalanceFragment;
import com.hunk.nobank.contract.AccountModel;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.AccountType;
import com.hunk.nobank.contract.Money;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.type.LoginStateEnum;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.UserSession;
import com.hunk.test.utils.NBAbstractTest;
import com.hunk.test.utils.NetworkHandlerStub;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class BalanceFragmentTest extends NBAbstractTest {
    private NetworkHandlerStub mNetworkHandlerStub;

    @Before
    public void prepare() {
        // set next response
        mNetworkHandlerStub =
                (NetworkHandlerStub) Core.getInstance().getNetworkHandler();

        mNetworkHandlerStub.clear();
    }

    @Test
    public void testShowBalance() {
        final UserManager userManager = new UserManager(null);
        UserSession userSession = new UserSession();
        userSession.setLoginState(LoginStateEnum.Logined);
        userManager.setCurrentUserSession(userSession);
        Core.getInstance().setLoginManager(userManager);

        AccountModel accountModel = new AccountModel();
        accountModel.Balance = new Money("9999.99");
        accountModel.Type = AccountType.Main;
        AccountSummary accountSummary = new AccountSummary();
        accountSummary.Accounts = new ArrayList<>();
        accountSummary.Accounts.add(accountModel);
        RealResp<AccountSummary> realResp = new RealResp<>();
        realResp.Response = accountSummary;
        mNetworkHandlerStub.setNextResponse(realResp);

        BalanceFragment balanceFragment = new BalanceFragment();
        SupportFragmentTestUtil.startFragment(balanceFragment);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        Assert.assertNotNull(balanceFragment);
        Assert.assertNotNull(balanceFragment.getView());
        TextView balanceText = (TextView) balanceFragment.getView().findViewById(R.id.txt_balance);
        Assert.assertEquals("9999.99", balanceText.getText().toString());
    }
}
