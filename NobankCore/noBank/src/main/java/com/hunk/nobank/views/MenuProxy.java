package com.hunk.nobank.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.util.ViewHelper;
import com.hunk.whitelabel.Feature;

import java.util.ArrayList;
import java.util.List;

public class MenuProxy {
    private final MenuBaseAdapter mMenuListAdapter;
    private final DrawerLayout mDrawLayout;
    private TextView mHelloUser;
    private TextView mLogOut;
    private ListView mMenuList;
    private Context mCtx;

    public MenuProxy(View menu, DrawerLayout mDrawLayout) {
        this.mCtx = menu.getContext();
        mHelloUser = (TextView) menu.findViewById(R.id.menu_hello_user).findViewById(R.id.menu_button);
        mLogOut = (TextView) menu.findViewById(R.id.menu_log_out).findViewById(R.id.menu_button);

        mMenuList = (ListView) menu.findViewById(R.id.menu_list);
        mMenuListAdapter = new MenuBaseAdapter(menu.getContext(), 0, new ArrayList<MenuButton>(), mDrawLayout);
        mMenuList.setAdapter(mMenuListAdapter);
        mMenuList.setOnItemClickListener(mMenuListAdapter);

        this.mDrawLayout = mDrawLayout;
    }

    /**
     * PrepareM menu button "when we are sure to show it".
     */
    public void prepareMenuButtons() {
        mHelloUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hello, 0, 0, 0);
        mHelloUser.setText(R.string.hello_user);

        mLogOut.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_log_out, 0, 0, 0);
        mLogOut.setText(R.string.menu_log_out);
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.unrollActivity(v.getContext());
            }
        });

        mMenuListAdapter.add(
                new MenuButton(R.drawable.ic_dashboard, R.string.dashboard,
                        BaseActivity.generateAction(Feature.dashboard, NConstants.OPEN_MAIN)));
        mMenuListAdapter.add(
                new MenuButton(R.drawable.ic_dashboard, R.string.pay,
                        BaseActivity.generateAction(Feature.pay, NConstants.OPEN_MAIN)));
    }

    /**
     * Menu prototype
     */
    private static class MenuButton {
        int iconId;
        int menuTxtId;
        String action;

        public MenuButton(int iconId, int menuTxtId, String action) {
            this.iconId = iconId;
            this.menuTxtId = menuTxtId;
            this.action = action;
        }
    }

    private static class MenuBaseAdapter extends ArrayAdapter<MenuButton> implements AdapterView.OnItemClickListener {

        private final DrawerLayout mDrawLayout;

        public MenuBaseAdapter(Context context, int resource, List<MenuButton> objects, DrawerLayout mDrawLayout) {
            super(context, resource, objects);
            this.mDrawLayout = mDrawLayout;
        }

        /**
         * View Holder
         */
        private static class ViewHolder {
            TextView menuButton;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MenuButton button = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
                convertView = inflater.inflate(R.layout.item_menu_button, parent, false);
                ViewHelper.updateFontsStyle((ViewGroup) convertView);

                holder = new ViewHolder();
                holder.menuButton = (TextView) convertView.findViewById(R.id.menu_button);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.menuButton.setText(button.menuTxtId);
            holder.menuButton.setCompoundDrawablesWithIntrinsicBounds(button.iconId, 0, 0, 0);

            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MenuButton button = getItem(position);

            Intent toScreenIntent = BaseActivity.getUnrollIntent(view.getContext().getPackageName());
            toScreenIntent.putExtra(NConstants.INTENT_EXTRA_IS_START_MENU, true);
            toScreenIntent.putExtra(NConstants.INTENT_EXTRA_START_MENU, button.action);

            view.getContext().startActivity(toScreenIntent);

            mDrawLayout.closeDrawer(Gravity.LEFT);
        }
    }
}
