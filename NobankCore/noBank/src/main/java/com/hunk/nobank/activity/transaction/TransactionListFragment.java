package com.hunk.nobank.activity.transaction;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.hunk.nobank.Core;
import com.hunk.nobank.R;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.extension.view.NBProgressView;
import com.hunk.nobank.manager.ManagerListener;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.ViewManagerListener;
import com.hunk.nobank.model.TransactionReqPackage;
import com.hunk.nobank.views.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class TransactionListFragment extends Fragment {

    private PullToRefreshListView mTransactionList;
    private UserManager mUserManager;
    private TransactionDataManager mTransactionDataMgr;
    private TransactionListAdapter mTransactionListAdapter;
    private LoadingState mLoadingState;
    private boolean mIsFirstTime = true;
    private NBProgressView mLoadingView;

    public TransactionListFragment() {
        super();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        bindingListener();
        setupUI(view);
        return view;
    }

    private void bindingListener() {
        mUserManager = Core.getInstance().getLoginManager();
        mTransactionDataMgr = mUserManager.getTransactionDataManager();
    }

    private void setupUI(View root) {
        mTransactionList = (PullToRefreshListView) root.findViewById(R.id.transaction_list);
        mTransactionListAdapter = new TransactionListAdapter(root.getContext(), 0,
                new ArrayList<ViewTransactionFields>());
        mTransactionList.setAdapter(mTransactionListAdapter);
        mTransactionList.setListListener(new PullToRefreshListView.ListListener() {
            @Override
            public void refresh() {
                mLoadingState = LoadingState.PULL_TO_REFRESH;
                mMoreView.setDisable(true);
                // Force fetch when pull the list view
                TransactionReqPackage.cache.expire();
                mTransactionDataMgr.fetchTransactions(false, mManagerListener);
            }

            @Override
            public void more() {
            }
        });
        mTransactionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewTransactionFields viewTransactionFields = mTransactionListAdapter.getItem(position - 1);
                viewTransactionFields.onClick(view);
            }
        });
        mLoadingView = (NBProgressView) root.findViewById(R.id.loading_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsFirstTime) {
            mIsFirstTime = false;
            mLoadingState = LoadingState.INIT_REFRESH;
            mLoadingView.startMyAnimation();
            mTransactionDataMgr.fetchTransactions(false, mManagerListener);
        }
    }

    ManagerListener mManagerListener = new ViewManagerListener(this) {
        @Override
        public void onSuccess(String managerId, String messageId, Object data) {
            if (managerId.equals(mTransactionDataMgr.getManagerId())) {
                if (messageId.equals(TransactionDataManager.METHOD_TRANSACTION)) {
                    switch (mLoadingState) {
                        case MORE_REFRESH:
                            mMoreView.reset();
                            mTransactionList.setPullable(true);
                            break;
                        case PULL_TO_REFRESH:
                            mMoreView.setDisable(false);
                            break;
                        case INIT_REFRESH:
                            mLoadingView.stopAnimation();
                            mLoadingView.setVisibility(View.GONE);
                            mTransactionList.setVisibility(View.VISIBLE);
                            break;
                    }
                    mLoadingState = LoadingState.UNKNOWN;
                    refreshListData();
                }
            }
        }

        private void refreshListData() {
            mTransactionListAdapter.clear();
            List<ViewTransactionFields> newList = addRawTransactionFields(TransactionReqPackage.cache.get().Response);
            for (ViewTransactionFields fields : newList) {
                mTransactionListAdapter.add(fields);
            }
            mTransactionListAdapter.notifyDataSetChanged();
            mTransactionList.hideHeaderView();
        }

        @Override
        public void onFailed(String managerId, String messageId, Object data) {

        }
    };

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
                    mLoadingState = LoadingState.MORE_REFRESH;
                    mTransactionList.setPullable(false);
                    mTransactionDataMgr.fetchTransactions(true, mManagerListener);
                }
                super.onClick(v);
            }
        }
    };

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
}
