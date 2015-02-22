package com.hunk.nobank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.hunk.nobank.feature.Feature;
import com.hunk.nobank.util.Logging;
import com.hunk.nobank.views.LoadingDialogFragment;

public class BaseActivity extends ActionBarActivity {
	
	public final static String ACTION_GOTO_ROOT = ".action.goto.root";
    private static final String DIALOG_LOADING_TAG = "DIALOG_LOADING_TAG";

    private FrameLayout mContentContainer;
    protected NoBankApplication application;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (NoBankApplication)getApplication();

        setupUI();
	}

    private void setupUI() {
        // get root container
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_base_with_titlebar, null, false);
        mContentContainer = (FrameLayout) view.findViewById(R.id.activity_base_main_content);
        super.setContentView(view);
    }

    public void unrollActivity() {
		String packageName = this.getApplication().getPackageName();
		
		Intent unroll = new Intent();
		unroll.setAction(packageName + ACTION_GOTO_ROOT);
		unroll.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(unroll);
	}

    protected String generateAction(Feature feature, String realAction) {
        return String.format("action.%s.%s", feature.toString(), realAction);
    }

    @Override
    public void startActivity(Intent intent) {
        Logging.d("go to screen : " + intent.getAction());
        super.startActivity(intent);
    }

    public void showLoading() {
        LoadingDialogFragment frag = LoadingDialogFragment.newInstance();
        FragmentManager fragMgr = getSupportFragmentManager();
        frag.show(fragMgr.beginTransaction(), DIALOG_LOADING_TAG);
    }

    public void dismissLoading() {
        FragmentManager fragMgr = getSupportFragmentManager();
        LoadingDialogFragment frag = (LoadingDialogFragment)fragMgr.findFragmentByTag(DIALOG_LOADING_TAG);
        frag.dismiss();
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutResID, null, false);
        mContentContainer.addView(view);
    }
}
