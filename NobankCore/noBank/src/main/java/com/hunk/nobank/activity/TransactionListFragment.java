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
                new ArrayList<TransactionListAdapter.ViewTransactionFields>());
        mTransactionList.setAdapter(mTransactionListAdapter);
        mTransactionList.setListListener(new PullToRefreshListView.ListListener() {
            @Override
            public void refresh() {
                // Force fetch when pull the list view
                TransactionReqPackage.cache.expire();
                mTransactionDataMgr.fetchTransactions(false, mManagerListener);
            }

            @Override
            public void more() {
                mTransactionDataMgr.fetchTransactions(true, mManagerListener);
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
                    mTransactionListAdapter.addRawTransactionFields(TransactionReqPackage.cache.get().Response);
                    mTransactionListAdapter.notifyDataSetChanged();
                    mTransactionList.hideHeaderView();
                }
            }
        }

        @Override
        public void onFailed(String managerId, String messageId, Object data) {

        }
    };

    public static class TransactionListAdapter extends ArrayAdapter<TransactionListAdapter.ViewTransactionFields> {

        public TransactionListAdapter(Context context, int resource, List<ViewTransactionFields> objects) {
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

            ViewTransactionFields fields = getItem(position);
            if (fields != null) {
                switch (fields.getViewType()) {
                    case PAY:
                    case DEPOSIT:
                    case VAULT:
                        viewHolder.mTitle.setText(fields.getTransactionFields().getTitle());
                        viewHolder.mMoney.setText(String.valueOf(fields.getTransactionFields().getMoney()));
                        break;
                    case MORE:
                        viewHolder.mTitle.setText("MORE");
                        break;
                }
            }
            return convertView;
        }

        /**
         * 1. Convert TransactionFields to ViewTransactionFields
         * 2. Sort them
         * 3. Add date label
         * 4. Add more button
         * @param fields
         */
        public void addRawTransactionFields(List<TransactionFields> fields) {
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
                ViewTransactionFields newField = new ViewTransactionFields(type, raw);
                newList.add(newField);
            }
            newList.add(new ViewTransactionFields(ViewTransactionType.MORE, null));

            for (ViewTransactionFields newField : newList) {
                add(newField);
            }
        }

        static class ViewHolder {
            TextView mTitle;
            TextView mMoney;
            TextView mType;
        }

        public enum ViewTransactionType {
            DATE, MORE, PAY, VAULT, DEPOSIT
        }

        public static class ViewTransactionFields {
            ViewTransactionType mViewType;
            TransactionFields mTransactionFields;

            public ViewTransactionFields(ViewTransactionType viewType, TransactionFields transactionFields) {
                this.mViewType = viewType;
                this.mTransactionFields = transactionFields;
            }

            public TransactionFields getTransactionFields() {
                return mTransactionFields;
            }

            public ViewTransactionType getViewType() {
                return mViewType;
            }
        }
    }
}
