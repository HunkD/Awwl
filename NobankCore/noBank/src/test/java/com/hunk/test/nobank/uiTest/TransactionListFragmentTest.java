package com.hunk.test.nobank.uiTest;

import android.widget.ListView;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.TransactionListFragment;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.contract.TransactionType;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.activity.TransactionListFragment.TransactionListAdapter.ViewTransactionFields;
import com.hunk.test.utils.NBAbstractTest;
import com.hunk.test.utils.NetworkHandlerStub;
import com.hunk.test.utils.TestNoBankApplication;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class)
public class TransactionListFragmentTest extends NBAbstractTest {
    private NetworkHandlerStub mNetworkHandlerStub;
    private UserManager mUserManager;
    private TransactionDataManager mTransactionDataManager;

    @Before
    public void prepare() {
        // set next response
        mNetworkHandlerStub =
                (NetworkHandlerStub) Core.getInstance().getNetworkHandler();

        mNetworkHandlerStub.clear();

        mTransactionDataManager = new TransactionDataManager(null);
        mUserManager = Mockito.mock(UserManager.class);
        Mockito.when(mUserManager.getTransactionDataManager())
                .thenReturn(mTransactionDataManager);
        Core.getInstance().setLoginManager(mUserManager);
    }

    @Test
    public void showTransactionList() {
        // Mock data
        List<TransactionFields> list = new ArrayList<>();
        list.add(new TransactionFields("Move to vault", 15.5, TransactionType.VAULT, 1000));
        list.add(new TransactionFields("Pay to Hunk", 19.5, TransactionType.PAY, 1000));
        list.add(new TransactionFields("Deposit from check", 25.5, TransactionType.DEPOSIT, 1000));
        RealResp<List<TransactionFields>> realResp = new RealResp<>();
        realResp.Response = list;
        mNetworkHandlerStub.setNextResponse(realResp);

        // Begin test
        TransactionListFragment transactionListFragment = new TransactionListFragment();
        SupportFragmentTestUtil.startFragment(transactionListFragment);

        Assert.assertNotNull(transactionListFragment);
        Assert.assertNotNull(transactionListFragment.getView());

        ListView transactionListView = (ListView) transactionListFragment.getView().findViewById(R.id.transaction_list);
        Assert.assertEquals("Move to vault",
                ((ViewTransactionFields) transactionListView.getAdapter().getItem(1)).getTransactionFields().getTitle());

        Assert.assertEquals(list.size() + 2, transactionListView.getAdapter().getCount());
    }
}
