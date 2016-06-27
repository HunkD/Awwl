package com.hunk.nobank.activity.dashboard.transaction.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hunk.abcd.extension.font.UpdateFont;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.dashboard.transaction.ViewTransactionFields;
import com.hunk.nobank.activity.dashboard.transaction.ViewTransactionType;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.abcd.extension.util.ViewHelper;

/**
 *
 */
public class MoreView extends ViewTransactionFields {
    private boolean mIsFetching;
    private boolean mDisable;

    public MoreView(ViewTransactionType type, TransactionFields raw) {
        super(type, raw);
    }

    @Override
    public View render(Context context, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_transaction_more, parent, false);

            UpdateFont.updateFontsStyle((ViewGroup) convertView);

            viewHolder = new ViewHolder();
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mIsFetching) {
            viewHolder.mTitle.setText(R.string.more_fetching);
        } else {
            viewHolder.mTitle.setText(R.string.more);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (!mIsFetching) {
            mIsFetching = true;
            updateView(v);
        }
    }

    private void updateView(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag();
        viewHolder.mTitle.setText(R.string.more_fetching);
    }

    public boolean isFetching() {
        return mIsFetching;
    }

    public void reset() {
        mIsFetching = false;
    }

    public void setDisable(boolean mDisable) {
        this.mDisable = mDisable;
    }

    public boolean getDisable() {
        return mDisable;
    }

    static class ViewHolder {
        TextView mTitle;
    }
}
