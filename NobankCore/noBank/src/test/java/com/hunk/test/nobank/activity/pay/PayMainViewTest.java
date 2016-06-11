package com.hunk.test.nobank.activity.pay;

import com.hunk.nobank.activity.payment.PayMainPresenter;
import com.hunk.nobank.activity.payment.PayMainView;
import com.hunk.nobank.activity.payment.PayMainViewImplActivity;
import com.hunk.test.utils.AfterLoginTest;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.robolectric.Robolectric.setupActivity;

/**
 * Created by HunkDeng on 2016/6/10.
 */
public class PayMainViewTest extends AfterLoginTest
        implements PayMainView<PayMainPresenter> {

    @Test
    public void smoke() {
        getTestObj();
    }

    private PayMainView<PayMainPresenter> getTestObj() {
        PayMainView<PayMainPresenter> view = setupActivity(PayMainViewImplActivity.class);
        view.setPresenter(getPresenter());
        return view;
    }

    @Override
    public void setPresenter(PayMainPresenter presenter) {

    }

    @Override
    public PayMainPresenter getPresenter() {
        return mock(PayMainPresenter.class);
    }
}
