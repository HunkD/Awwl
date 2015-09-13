package com.hunk.nobank.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hunk.nobank.Core;
import com.hunk.nobank.R;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.manager.ManagerListener;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.ViewManagerListener;
import com.hunk.nobank.model.TransactionReqPackage;
import com.hunk.nobank.util.ViewHelper;
import com.hunk.nobank.views.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class TransactionListFragment extends Fragment {

    private PullToRefreshListView mTransactionList;
    private UserManager mUserManager;
    private TransactionDataManager mTransactionDataMgr;
    private TransactionListAdapter mTransactionListAdapter;

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
                new ArrayList<TransactionFields>());
        mTransactionList.setAdapter(mTransactionListAdapter);
        mTransactionList.setListListener(new PullToRefreshListView.ListListener() {
            @Override
            public void refresh() {
                mTransactionDataMgr.fetchTransactions(false, mManagerListener);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        mTransactionDataMgr.fetchTransactions(false, mManagerListener);
    }

    ManagerListener mManagerListener = new ViewManagerListener(this) {
        @Override
        public void onSuccess(String managerId, String messageId, Object data) {
            if (managerId.equals(mTransactionDataMgr.getManagerId())) {
                if (messageId.equals(TransactionDataManager.METHOD_TRANSACTION)) {
                    mTransactionListAdapter.clear();
                    for (TransactionFields fields : TransactionReqPackage.cache.get().Response) {
                        mTransactionListAdapter.add(fields);
                    }
                    mTransactionListAdapter.notifyDataSetChanged();
                    mTransactionList.hideHeaderView();
                }
            }
        }

        @Override
        public void onFailed(String managerId, String messageId, Object data) {

        }
    };

    private static class TransactionListAdapter extends ArrayAdapter<TransactionFields> {

        public TransactionListAdapter(Context context, int resource, List<TransactionFields> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_transaction, parent, false);

                ViewHelper.updateFontsStyle((ViewGroup) convertView);

                viewHolder = new ViewHolder();
                viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
                viewHolder.mMoney = (TextView) convertView.findViewById(R.id.money);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            TransactionFields fields = getItem(position);
            if (fields != null) {
                viewHolder.mTitle.setText(fields.getTitle());
                viewHolder.mMoney.setText(String.valueOf(fields.getMoney()));
            }
            return convertView;
        }

        static class ViewHolder {
            TextView mTitle;
            TextView mMoney;
            TextView mType;
        }
    }
}
