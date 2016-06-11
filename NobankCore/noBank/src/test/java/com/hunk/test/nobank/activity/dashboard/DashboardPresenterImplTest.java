package com.hunk.test.nobank.activity.dashboard;

import com.hunk.nobank.activity.dashboard.DashboardPresenter;
import com.hunk.nobank.activity.dashboard.DashboardPresenterImpl;
import com.hunk.nobank.activity.dashboard.DashboardView;
import com.hunk.nobank.contract.AccountModel;
import com.hunk.nobank.contract.Money;
import com.hunk.nobank.manager.AccountDataManager;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.dataBasic.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.nobank.model.Cache;
import com.hunk.test.utils.AfterLoginTest;

import org.junit.Test;
import org.robolectric.util.ReflectionHelpers;

import static org.junit.Assert.assertNotNull;
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
public class DashboardPresenterImplTest extends AfterLoginTest implements DashboardPresenter<DashboardView> {

    interface ReflectionId {
        String view = "mView";
        String viewManagerListener = "mViewManagerListener";
    }
    
    private DashboardPresenter<DashboardView> getTestObj() {
        DashboardPresenter<DashboardView> presenter = new DashboardPresenterImpl();
        presenter.attach(getView());
        return presenter;
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

        DashboardPresenter<DashboardView> presenter = getTestObj();
        presenter.onResume();

        verify(presenter.getView(), times(1)).showLoadingBalance();
    }
    
    @Test
    public void onResumeWithCache() {
        //
        AccountSummaryPackage.cache = new Cache<>();
        AccountSummaryPackage.cache.expire();
        //
        DashboardPresenter<DashboardView> presenter = getTestObj();
        presenter.onResume();
        //
        verify(presenter.getView(), times(0)).showLoadingBalance();
    }

    @Test
    public void accountSummaryActionSuccess() {
        AccountModel accountModel = new AccountModel();
        accountModel.Balance = new Money("20");
        when(getMockedUS().getAccountDataManager())
                .thenReturn(new AccountDataManager(accountModel));
        //
        DashboardPresenter<DashboardView> presenter = getTestObj();
        ViewManagerListener viewManagerListener =
                ReflectionHelpers.getField(presenter, ReflectionId.viewManagerListener);

        viewManagerListener.onSuccess(UserManager.MANAGER_ID, UserManager.METHOD_ACCOUNT_SUMMARY, null);
        //
        verify(presenter.getView(), times(1)).showBalance(accountModel.Balance);
    }

    @Test
    public void accountSummaryActionFailed() {
        DashboardPresenter<DashboardView> presenter = getTestObj();
        DashboardView mockedView = presenter.getView();
        presenter.detach();
        ViewManagerListener viewManagerListener =
                ReflectionHelpers.getField(presenter, ReflectionId.viewManagerListener);

        viewManagerListener.onFailed(UserManager.MANAGER_ID, UserManager.METHOD_ACCOUNT_SUMMARY, null);
        //
        verify(mockedView, times(0)).showBalance(any(Money.class));
    }

    @Test
    @Override
    public void forceRefreshAction() {
        DashboardPresenter<DashboardView> presenter = getTestObj();
        presenter.forceRefreshAction();

        TransactionDataManager mockedTM = getMockedTM();
        verify(mockedTM, times(1)).fetchTransactions(eq(false), any(ViewManagerListener.class));
    }

    @Test
    @Override
    public void firstTimeResume() {
        DashboardPresenter<DashboardView> presenter = getTestObj();
        presenter.firstTimeResume();

        TransactionDataManager mockedTM = getMockedTM();
        verify(mockedTM, times(1)).fetchTransactions(eq(false), any(ViewManagerListener.class));
    }

    @Override
    public void showMoreTransactionsAction() {
        DashboardPresenter<DashboardView> presenter = getTestObj();
        presenter.showMoreTransactionsAction();

        TransactionDataManager mockedTM = getMockedTM();
        verify(mockedTM, times(1)).fetchTransactions(eq(true), any(ViewManagerListener.class));
    }

    @Override
    public void attach(DashboardView view) {
        DashboardPresenter<DashboardView> presenter = getTestObj();
        DashboardView dashboardView = presenter.getView();
        presenter.detach();
        assertNull(presenter.getView());

        presenter.attach(dashboardView);
        assertNotNull(presenter.getView());
    }

    @Test
    @Override
    public void detach() {
        DashboardPresenter<DashboardView> presenter = getTestObj();
        presenter.detach();
        assertNull(presenter.getView());
    }

    @Override
    public DashboardView getView() {
        return mock(DashboardView.class);
    }
}
