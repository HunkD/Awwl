package com.hunk.test.nobank.activity.dashboard;

import android.widget.TextView;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.dashboard.DashboardActivity;
import com.hunk.nobank.contract.Money;
import com.hunk.test.utils.AfterLoginTest;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Robolectric.setupActivity;

/**
 * @author HunkDeng
 * @since 2016/5/23
 */
@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class DashboardActivityTest extends AfterLoginTest {

    @Test
    public void testShowBalance() {
        DashboardActivity activity = setupActivity(DashboardActivity.class);
        TextView balanceView = ReflectionHelpers.getField(activity, "mBalance");
        activity.showBalance(new Money("20.00"));
        assertEquals("20.00", balanceView.getText().toString());
    }

    @Test
    public void testShowLoadingBalance() {
        DashboardActivity activity = setupActivity(DashboardActivity.class);
        activity.showLoadingBalance();
        TextView balanceView = ReflectionHelpers.getField(activity, "mBalance");
        assertEquals(activity.getString(R.string.loading_balance), balanceView.getText());
    }
}
