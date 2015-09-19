package com.hunk.test.nobank.uiTest;

import android.widget.TextView;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BalanceFragment;
import com.hunk.nobank.contract.AccountModel;
import com.hunk.nobank.contract.AccountType;
import com.hunk.nobank.contract.Money;
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
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import org.robolectric.util.ReflectionHelpers;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class)
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
        AccountModel accountModel = new AccountModel();
        accountModel.Balance = new Money("9999.99");
        AccountDataManager accountDataManager = Mockito.mock(AccountDataManager.class);
        Mockito.when(accountDataManager.getAccountModel()).thenReturn(accountModel);
        final UserManager userManager = Mockito.mock(UserManager.class);
        Mockito.when(userManager.getAccountDataManagerByType(AccountType.Main)).thenReturn(accountDataManager);
        Core.getInstance().setLoginManager(userManager);

        BalanceFragment balanceFragment = new BalanceFragment();

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ViewManagerListener managerListener = (ViewManagerListener) invocation.getArguments()[1];
                managerListener.success(
                        ReflectionHelpers.getStaticField(UserManager.class, "MANAGER_ID").toString(),
                        UserManager.METHOD_ACCOUNT_SUMMARY,
                        null);
                return null;
            }
        }).when(userManager).fetchAccountSummary(
                Mockito.any(AccountSummaryPackage.class),
                Mockito.any(ViewManagerListener.class));

        SupportFragmentTestUtil.startFragment(balanceFragment);

        Assert.assertNotNull(balanceFragment);
        Assert.assertNotNull(balanceFragment.getView());
        TextView balanceText = (TextView) balanceFragment.getView().findViewById(R.id.txt_balance);
        Assert.assertEquals("9999.99", balanceText.getText().toString());
    }
}
