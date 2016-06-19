package com.hunk.nobank.activity.dashboard.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunk.nobank.R;
import com.hunk.nobank.contract.TransactionCategory;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.extension.network.ServerConfig;
import com.hunk.nobank.util.Hmg;
import com.hunk.nobank.util.ViewHelper;

/**
 *
 */
public class NormalViewTransactionFields extends ViewTransactionFields  {
    private final Hmg mHmg;

    public NormalViewTransactionFields(ViewTransactionType viewType, TransactionFields transactionFields) {
        super(viewType, transactionFields);
        mHmg = Hmg.getInstance();
    }

    @Override
    public View render(Context context, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_transaction, parent, false);

            ViewHelper.updateFontsStyle((ViewGroup) convertView);

            viewHolder = new ViewHolder();
            viewHolder.mIcon = (ImageView) convertView.findViewById(R.id.transaction_item_icon);
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.mMoney1 = (TextView) convertView.findViewById(R.id.money_1);
            viewHolder.mMoney2 = (TextView) convertView.findViewById(R.id.money_2);
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mIcon.setImageResource(getIconSource());
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
        // load img
        String imgId = getTransactionFields().getImageId();
        mHmg.load(imgId, viewHolder.mImg, R.drawable.ic_launcher);

        return convertView;
    }

    public int getIconSource() {
        return R.drawable.ic_hello;
    }

    @Override
    public void onClick(View v) {

    }


    static class ViewHolder {
        ImageView mIcon;
        TextView mTitle;
        TextView mMoney2;
        TextView mMoney1;
        TextView mType;
        ImageView mImg;
    }
}
