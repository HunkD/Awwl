package com.hunk.nobank.activity.dashboard.transaction;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 */
public interface Rendering {
    View render(Context context, int position, View convertView, ViewGroup parent);
}
