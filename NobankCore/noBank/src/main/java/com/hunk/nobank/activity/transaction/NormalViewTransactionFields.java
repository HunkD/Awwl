package com.hunk.nobank.activity.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hunk.nobank.R;
import com.hunk.nobank.contract.TransactionCategory;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.util.ViewHelper;

/**
 *
 */
public class NormalViewTransactionFields extends ViewTransactionFields  {
    public NormalViewTransactionFields(ViewTransactionType viewType, TransactionFields transactionFields) {
        super(viewType, transactionFields);
    }

    @Override
    public View render(Context context, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_transaction, parent, false);

            ViewHelper.updateFontsStyle((ViewGroup) convertView);

            viewHolder = new ViewHolder();
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.mMoney1 = (TextView) convertView.findViewById(R.id.money_1);
            viewHolder.mMoney2 = (TextView) convertView.findViewById(R.id.money_2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.mTitle.setText(getTransactionFields().getTitle());
        if (TransactionCategory.Debit == getTransactionFields().getCategory()) {
            viewHolder.mMoney2.setVisibility(View.VISIBLE);
            viewHolder.mMoney2.setText("-" + String.valueOf(getTransactionFields().getMoney()) + "RMB");
            viewHolder.mMoney1.setVisibility(View.GONE);
        } else {
            viewHolder.mMoney1.setVisibility(View.VISIBLE);
            viewHolder.mMoney1.setText("+" + String.valueOf(getTransactionFields().getMoney()) + "RMB");
            viewHolder.mMoney2.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }


    static class ViewHolder {
        TextView mTitle;
        TextView mMoney2;
        TextView mMoney1;
        TextView mType;
    }
}
