package com.hunk.nobank.activity.dashboard.transaction.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunk.abcd.extension.font.UpdateFont;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.dashboard.transaction.NormalViewTransactionFields;
import com.hunk.nobank.activity.dashboard.transaction.ViewTransactionType;
import com.hunk.nobank.contract.TransactionCategory;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.abcd.extension.util.ViewHelper;

import java.util.Locale;

/**
 *
 */
public class PayView extends NormalViewTransactionFields {
    public PayView(ViewTransactionType type, TransactionFields raw) {
        super(type, raw);
    }

    @Override
    public View render(Context context, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_transaction_p2p, parent, false);

            UpdateFont.updateFontsStyle((ViewGroup) convertView);

            viewHolder = new ViewHolder();
            viewHolder.mIcon = (TextView) convertView.findViewById(R.id.transaction_item_icon);
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.mMoney1 = (TextView) convertView.findViewById(R.id.money_1);
            viewHolder.mMoney2 = (TextView) convertView.findViewById(R.id.money_2);
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String object = getTransactionFields().getObject();
        if (object != null && object.toCharArray().length >= 1) {
            String firstChar = String.valueOf(object.toUpperCase(Locale.US).charAt(0));
            viewHolder.mIcon.setText(firstChar);
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

    static class ViewHolder {
        TextView mIcon;
        TextView mTitle;
        TextView mMoney2;
        TextView mMoney1;
        TextView mType;
        ImageView mImg;
    }
}
