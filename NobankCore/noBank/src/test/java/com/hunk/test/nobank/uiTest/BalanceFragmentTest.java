package com.hunk.test.nobank.uiTest;

import android.widget.TextView;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BalanceFragment;
import com.hunk.nobank.contract.AccountModel;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.AccountType;
import com.hunk.nobank.contract.Money;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.manager.AccountDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.test.utils.NBAbstractTest;
import com.hunk.test.utils.NetworkHandlerStub;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import org.robolectric.util.ReflectionHelpers;

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

        Assert.assertNotNull(balanceFragment);
        Assert.assertNotNull(balanceFragment.getView());
        TextView balanceText = (TextView) balanceFragment.getView().findViewById(R.id.txt_balance);
        Assert.assertEquals("9999.99", balanceText.getText().toString());
    }
}
