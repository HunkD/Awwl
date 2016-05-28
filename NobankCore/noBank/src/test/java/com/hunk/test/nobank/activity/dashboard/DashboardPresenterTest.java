package com.hunk.test.nobank.activity.dashboard;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.activity.dashboard.DashboardPresenter;
import com.hunk.nobank.activity.dashboard.DashboardView;
import com.hunk.nobank.contract.AccountModel;
import com.hunk.nobank.contract.AccountType;
import com.hunk.nobank.contract.Money;
import com.hunk.nobank.manager.AccountDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.dataBasic.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.nobank.model.Cache;
import com.hunk.test.utils.AfterLoginTest;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author HunkDeng
 * @since 2016/5/24
 */
@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class DashboardPresenterTest extends AfterLoginTest {
    private DashboardView mMockedView;

    @Before
    public void setup() {
        super.setup();
        mMockedView = mock(DashboardView.class);
    }

    @Test
    public void testOnResumeWithoutCache() {
        when(getMockedUM().fetchAccountSummary(
                any(AccountSummaryPackage.class), any(ViewManagerListener.class))).thenReturn(true);

        DashboardPresenter presenter = new DashboardPresenter(mMockedView);
        presenter.onResume();

        verify(mMockedView, times(1)).showLoadingBalance();
    }

    @Test
    public void testOnResumeWithCache() {
        //
        AccountSummaryPackage.cache = new Cache<>();
        AccountSummaryPackage.cache.expire();
        //
        DashboardPresenter presenter = new DashboardPresenter(mMockedView);
        presenter.onResume();
        //
        verify(mMockedView, times(0)).showLoadingBalance();
    }

    @Test
    public void testOnDestroy() {
        DashboardPresenter presenter = new DashboardPresenter(mMockedView);
        presenter.onDestroy();
        assertNull(ReflectionHelpers.getField(presenter, "mView"));
    }

    @Test
    public void testAccountSummaryActionSuccess() {
        AccountModel accountModel = new AccountModel();
        accountModel.Balance = new Money("20");
        when(getMockedUS().getAccountDataManagerByType(AccountType.Main))
                .thenReturn(new AccountDataManager(accountModel));
        //
        DashboardPresenter presenter = new DashboardPresenter(mMockedView);
        ViewManagerListener viewManagerListener =
                ReflectionHelpers.getField(presenter, "mViewManagerListener");

        viewManagerListener.onSuccess(UserManager.MANAGER_ID, UserManager.METHOD_ACCOUNT_SUMMARY, null);
        //
        verify(mMockedView, times(1)).showBalance(accountModel.Balance);
    }

    @Test
    public void testAccountSummaryActionFailed() {
        DashboardPresenter presenter = new DashboardPresenter(mMockedView);
        presenter.onDestroy();
        ViewManagerListener viewManagerListener =
                ReflectionHelpers.getField(presenter, "mViewManagerListener");

        viewManagerListener.onFailed(UserManager.MANAGER_ID, UserManager.METHOD_ACCOUNT_SUMMARY, null);
        //
        verify(mMockedView, times(0)).showBalance(any(Money.class));
    }
}
