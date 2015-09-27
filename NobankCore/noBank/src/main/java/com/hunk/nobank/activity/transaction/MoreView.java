package com.hunk.nobank.activity.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hunk.nobank.R;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.util.ViewHelper;

/**
 *
 */
public class MoreView extends ViewTransactionFields {
    public MoreView(ViewTransactionType type, TransactionFields raw) {
        super(type, raw);
    }

    @Override
    public View render(Context context, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_transaction_more, parent, false);

            ViewHelper.updateFontsStyle((ViewGroup) convertView);

            viewHolder = new ViewHolder();
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.mTitle.setText("MORE");
        return convertView;
    }


    static class ViewHolder {
        TextView mTitle;
    }
}
