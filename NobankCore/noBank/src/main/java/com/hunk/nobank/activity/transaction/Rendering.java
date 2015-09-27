package com.hunk.nobank.activity.transaction;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 */
public interface Rendering {
    View render(Context context, int position, View convertView, ViewGroup parent);
}
