package com.hunk.nobank.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hunk.nobank.R;
import com.hunk.nobank.model.TransactionFields;
import com.hunk.nobank.model.TransactionType;
import com.hunk.nobank.util.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public class TransactionListFragment extends Fragment {

    public TransactionListFragment() {
        super();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        setupUI(view);
        return view;
    }

    private void setupUI(View root) {
        ListView transactionList = (ListView) root.findViewById(R.id.transaction_list);
        transactionList.setAdapter(new TransactionListAdapter(root.getContext(), 0, getData()));
    }

    private List<TransactionFields> getData() {
        List<TransactionFields> list = new ArrayList<>();
        list.add(new TransactionFields("Move to vault", 15.5, TransactionType.VAULT));
        list.add(new TransactionFields("Pay to Hunk", 19.5, TransactionType.PAY));
        list.add(new TransactionFields("Deposit from check", 25.5, TransactionType.DEPOSIT));
        return list;
    }

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
