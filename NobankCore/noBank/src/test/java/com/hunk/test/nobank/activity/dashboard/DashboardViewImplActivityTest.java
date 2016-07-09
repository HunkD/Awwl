package com.hunk.test.nobank.activity.dashboard;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hunk.nobank.R;
import com.hunk.abcd.activity.mvp.BasePresenter;
import com.hunk.nobank.activity.dashboard.DashboardViewImplActivity;
import com.hunk.nobank.activity.dashboard.DashboardView;
import com.hunk.nobank.activity.dashboard.transaction.ViewTransactionFields;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.Money;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.test.utils.AfterLoginTest;
import com.hunk.test.utils.mock.MockTransaction;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.shadows.ShadowListView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.setupActivity;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.util.ReflectionHelpers.getField;

/**
 * @author HunkDeng
 * @since 2016/5/23
 */
public class DashboardViewImplActivityTest
        extends AfterLoginTest implements DashboardView {

    interface ReflectionId {
        String listView = "mListView";
        String balanceView = "mBalance";
        String swipeContainer = "mSwipeContainer";
    }

    @Override
    public void setup() {
        super.setup();
        when(getMockedUM().fetchAccountSummary())
                .thenReturn(Observable.just(new AccountSummary()));
        when(getMockedTM().fetchTransactions(anyBoolean()))
                .thenReturn(Observable.<List<TransactionFields>>just(new ArrayList<TransactionFields>()));
    }

    @Test
    public void showBalance() {
        showBalance(null);
    }

    public DashboardView getTestObj() {
        return setupActivity(DashboardViewImplActivity.class);
    }

    @Override
    public void showBalance(Money balance) {
        DashboardView dashboardView = getTestObj();
        TextView balanceView = getField(dashboardView, ReflectionId.balanceView);
        dashboardView.showBalance(new Money("20.00"));
        assertEquals("20.00", balanceView.getText().toString());
    }

    @Test
    @Override
    public void showLoadingBalance() {
        DashboardView dashboardView = getTestObj();
        dashboardView.showLoadingBalance();
        TextView balanceView = getField(dashboardView, ReflectionId.balanceView);
        assertEquals(getString(R.string.loading_balance), balanceView.getText());
    }

    /**
     * test interface redirect {@link #showTransactionList()}
     * @param mTransactionList
     */
    @Override
    public void showTransactionList(List mTransactionList) {
        // prepare
        List<TransactionFields> list = MockTransaction.fakeTransactionList();
        // test
        DashboardView dashboardView = getTestObj();
        dashboardView.showTransactionList(list);
        ListView listView = getField(dashboardView, ReflectionId.listView);
        ListAdapter listAdapter = listView.getAdapter();
        ShadowListView shadowListView = shadowOf(listView);
        shadowListView.populateItems();
        // verify
        int i = 0;
        for (TransactionFields transactionFields : list) {
            ViewTransactionFields viewTransactionFields = (ViewTransactionFields) listAdapter.getItem(i);
            compareObj(transactionFields, viewTransactionFields.getTransactionFields());
            i++;
        }
    }

    @Test
    public void showTransactionList() {
        showTransactionList(null);
    }

    @Test
    @Override
    public void showLoadingTransaction() {
        DashboardView dashboardView = getTestObj();
        dashboardView.showLoadingTransaction();
        SwipeRefreshLayout swipeContainer =
                getField(dashboardView, ReflectionId.swipeContainer);

        assertTrue(swipeContainer.isRefreshing());
    }


    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void showError(Throwable e) {

    }
}
