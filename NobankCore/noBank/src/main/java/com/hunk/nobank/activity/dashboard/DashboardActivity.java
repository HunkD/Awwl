package com.hunk.nobank.activity.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.activity.dashboard.transaction.TransactionViewFactory;
import com.hunk.nobank.activity.dashboard.transaction.ViewTransactionFields;
import com.hunk.nobank.activity.dashboard.transaction.ViewTransactionType;
import com.hunk.nobank.activity.dashboard.transaction.view.MoreView;
import com.hunk.nobank.contract.Money;
import com.hunk.nobank.contract.TransactionFields;

import java.util.ArrayList;
import java.util.List;

/**
 * Dashboard hold the balance view and transaction list
 *
 * Scenario 1:
 * When user came to this view first time, we will fetch balance and transaction data for them.
 *
 * Scenario 2:
 * When user pull down the transaction list view, we will force fetch transaction list.
 *
 * Scenario 3:
 *
 */
public class DashboardActivity extends BaseActivity implements DashboardView {
    private DashboardPresenter mPresenter;
    @VisibleForTesting
    private TextView mBalance;
    private SwipeRefreshLayout mSwipeContainer;
    private ListView mListView;
    private TransactionListAdapter mTransactionListAdapter;

    private boolean mInit = true;

    @Override
    protected boolean isRequiredLoginedUserSession() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_page, Base.NORMAL);
        // set Title
        getTitleBarPoxy().getTitle().setText(R.string.dashboard);
        mBalance = (TextView) findViewById(R.id.txt_balance);
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mListView = (ListView) findViewById(R.id.transaction_list);
        mTransactionListAdapter = new TransactionListAdapter(this, 0,
                new ArrayList<ViewTransactionFields>());
        mListView.setAdapter(mTransactionListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewTransactionFields viewTransactionFields = mTransactionListAdapter.getItem(position);
                viewTransactionFields.onClick(view);
            }
        });
        mSwipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.forceRefreshAction();
            }
        });
        //
        mPresenter = new DashboardPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
        if (mInit) {
            mInit = false;
            // This is the only way to show progress animation in the beginning.
            mSwipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeContainer.setRefreshing(true);
                }
            });
            //
            mPresenter.firstTimeResume();
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        BaseActivity.exitApplication(this);
    }

    @Override
    public void showBalance(Money balance) {
        mBalance.setText(balance.string());
    }

    @Override
    public void showLoadingBalance() {
        mBalance.setText(R.string.loading_balance);
    }

    @Override
    public void showTransactionList(List<TransactionFields> mTransactionList) {
        mTransactionListAdapter.clear();
        List<ViewTransactionFields> newList = addRawTransactionFields(mTransactionList);
        for (ViewTransactionFields fields : newList) {
            mTransactionListAdapter.add(fields);
        }
        mTransactionListAdapter.notifyDataSetChanged();
        mSwipeContainer.setRefreshing(false);
        mSwipeContainer.setEnabled(true);
    }

    @Override
    public void showLoadingTransaction() {
        mSwipeContainer.setRefreshing(true);
    }

    public static class TransactionListAdapter extends ArrayAdapter<ViewTransactionFields> {

        public TransactionListAdapter(Context context, int resource, List<ViewTransactionFields> objects) {
            super(context, resource, objects);
        }

        @Override
        public int getViewTypeCount() {
            return ViewTransactionType.values().length;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).getViewType().value;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).render(getContext(), position, convertView, parent);
        }
    }

    /**
     * 1. Convert TransactionFields to ViewTransactionFields
     * 2. Sort them
     * 3. Add date label
     * 4. Add more button
     * @param fields
     */
    public List<ViewTransactionFields> addRawTransactionFields(List<TransactionFields> fields) {
        List<ViewTransactionFields> newList = new ArrayList<>();
        for (TransactionFields raw : fields) {
            ViewTransactionType type = null;
            switch (raw.getVault()) {
                case PAY:
                    type = ViewTransactionType.PAY;
                    break;
                case DEPOSIT:
                    type = ViewTransactionType.DEPOSIT;
                    break;
                case VAULT:
                    type = ViewTransactionType.VAULT;
                    break;
            }
            ViewTransactionFields newField = TransactionViewFactory.getViewTransactionFields(type, raw);
            newList.add(newField);
        }
        newList.add(mMoreView);

        return newList;
    }

    private MoreView mMoreView = new MoreView(ViewTransactionType.MORE, null) {
        @Override
        public void onClick(View v) {
            if (!getDisable()) {
                if (!isFetching()) {
                    mSwipeContainer.setEnabled(false);
                    mPresenter.showMoreTransactionsAction();
                }
            }
        }
    };
}
