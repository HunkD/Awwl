package com.hunk.test.nobank.activity.dashboard;

import com.hunk.nobank.activity.dashboard.DashboardPresenter;
import com.hunk.nobank.activity.dashboard.DashboardPresenterImpl;
import com.hunk.nobank.activity.dashboard.DashboardView;
import com.hunk.nobank.contract.AccountModel;
import com.hunk.nobank.contract.AccountType;
import com.hunk.nobank.contract.Money;
import com.hunk.nobank.manager.AccountDataManager;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.dataBasic.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.nobank.model.Cache;
import com.hunk.test.utils.AfterLoginTest;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.util.ReflectionHelpers;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author HunkDeng
 * @since 2016/5/24
 */
public class DashboardPresenterImplTest extends AfterLoginTest implements DashboardPresenter {
    
    interface ReflectionId {
        String view = "mView";
        String viewManagerListener = "mViewManagerListener";
    }
    
    private DashboardView mMockedView;

    @Before
    public void setup() {
        super.setup();
        mMockedView = mock(DashboardView.class);
    }
    
    public DashboardPresenter getTestObj() {
        return new DashboardPresenterImpl(mMockedView);
    }

    /**
     * @see #onResumeWithCache()
     * @see #onResumeWithoutCache()
     */
    @Deprecated
    @Override
    public void onResume() {}
    
    @Test
    public void onResumeWithoutCache() {
        when(getMockedUM().fetchAccountSummary(
                any(AccountSummaryPackage.class), any(ViewManagerListener.class))).thenReturn(true);

        DashboardPresenter presenter = getTestObj();
        presenter.onResume();

        verify(mMockedView, times(1)).showLoadingBalance();
    }
    
    @Test
    public void onResumeWithCache() {
        //
        AccountSummaryPackage.cache = new Cache<>();
        AccountSummaryPackage.cache.expire();
        //
        DashboardPresenter presenter = getTestObj();
        presenter.onResume();
        //
        verify(mMockedView, times(0)).showLoadingBalance();
    }

    @Test
    public void accountSummaryActionSuccess() {
        AccountModel accountModel = new AccountModel();
        accountModel.Balance = new Money("20");
        when(getMockedUS().getAccountDataManagerByType(AccountType.Main))
                .thenReturn(new AccountDataManager(accountModel));
        //
        DashboardPresenter presenter = getTestObj();
        ViewManagerListener viewManagerListener =
                ReflectionHelpers.getField(presenter, ReflectionId.viewManagerListener);

        viewManagerListener.onSuccess(UserManager.MANAGER_ID, UserManager.METHOD_ACCOUNT_SUMMARY, null);
        //
        verify(mMockedView, times(1)).showBalance(accountModel.Balance);
    }

    @Test
    public void accountSummaryActionFailed() {
        DashboardPresenter presenter = getTestObj();
        presenter.onDestroy();
        ViewManagerListener viewManagerListener =
                ReflectionHelpers.getField(presenter, ReflectionId.viewManagerListener);

        viewManagerListener.onFailed(UserManager.MANAGER_ID, UserManager.METHOD_ACCOUNT_SUMMARY, null);
        //
        verify(mMockedView, times(0)).showBalance(any(Money.class));
    }

    @Test
    @Override
    public void onDestroy() {
        DashboardPresenter presenter = getTestObj();
        presenter.onDestroy();
        assertNull(ReflectionHelpers.getField(presenter, ReflectionId.view));
    }

    @Test
    @Override
    public void forceRefreshAction() {
        DashboardPresenter presenter = getTestObj();
        presenter.forceRefreshAction();

        TransactionDataManager mockedTM = getMockedTM();
        verify(mockedTM, times(1)).fetchTransactions(eq(false), any(ViewManagerListener.class));
    }

    @Test
    @Override
    public void firstTimeResume() {
        DashboardPresenter presenter = getTestObj();
        presenter.firstTimeResume();

        TransactionDataManager mockedTM = getMockedTM();
        verify(mockedTM, times(1)).fetchTransactions(eq(false), any(ViewManagerListener.class));
    }
}
