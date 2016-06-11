package com.hunk.nobank.activity.payment;

import android.os.Bundle;
import android.view.View;

import com.hunk.nobank.Core;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.flow.P2pScreenFlow;
import com.hunk.nobank.manager.flowBasic.ScreenFlow;

public class PayMainViewImplActivity
        extends BaseActivity<PayMainPresenter>
        implements PayMainView<PayMainPresenter> {

    {
        setPresenter(new PayMainPresenterImpl());
    }

    @Override
    protected boolean isRequiredLoginedUserSession() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_main, Base.NORMAL);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        unrollActivity(this);
    }

    public void onP2pClick(View view) {
        // build p2p screen flow
        ScreenFlow p2pScreenFlow = new P2pScreenFlow(Core.getInstance().getScreenFlowManager());
        // start screen flow
        p2pScreenFlow.start(this);
    }
}
