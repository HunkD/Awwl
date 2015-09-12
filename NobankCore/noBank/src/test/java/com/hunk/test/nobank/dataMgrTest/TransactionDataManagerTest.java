package com.hunk.test.nobank.dataMgrTest;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.contract.TransactionType;
import com.hunk.nobank.manager.ManagerListener;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.model.TransactionReqPackage;
import com.hunk.test.utils.NetworkHandlerStub;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
       application = TestNoBankApplication.class)
public class TransactionDataManagerTest {

    private NetworkHandlerStub mNetworkHandlerStub;

    @Before
    public void prepare() {
        // set next response
        mNetworkHandlerStub =
                (NetworkHandlerStub) Core.getInstance().getNetworkHandler();

        mNetworkHandlerStub.clear();
    }

    @Test
    public void testFetchTransactionWithoutCache() {
        TransactionDataManager transactionDataManager = new TransactionDataManager(null);

        TransactionFields transactionFields = new TransactionFields("XXX", 1000, TransactionType.DEPOSIT, 1000);
        RealResp<List<TransactionFields>> realResp = new RealResp<>();
        realResp.Response = new ArrayList<>();
        realResp.Response.add(transactionFields);

        mNetworkHandlerStub.setNextResponse(realResp);

        transactionDataManager.fetchTransactions(false, new ManagerListener() {
            @Override
            public void success(String managerId, String messageId, Object data) {
                RealResp<List<TransactionFields>> realResp = (RealResp<List<TransactionFields>>) data;
                Assert.assertEquals(1, realResp.Response.size());
                Assert.assertEquals("XXX", realResp.Response.get(0).getTitle());
            }

            @Override
            public void failed(String managerId, String messageId, Object data) {

            }
        });
    }

    @Test
    public void testFetchTransactionWithCache() {
        TransactionDataManager transactionDataManager = new TransactionDataManager(null);

        TransactionFields transactionFields = new TransactionFields("XXX", 1000, TransactionType.DEPOSIT, 1000);
        RealResp<List<TransactionFields>> realResp = new RealResp<>();
        realResp.Response = new ArrayList<>();
        realResp.Response.add(transactionFields);

        TransactionReqPackage.cache.setCache(realResp, null);

        transactionDataManager.fetchTransactions(false, new ManagerListener() {
            @Override
            public void success(String managerId, String messageId, Object data) {
                RealResp<List<TransactionFields>> realResp = (RealResp<List<TransactionFields>>) data;
                Assert.assertEquals(1, realResp.Response.size());
                Assert.assertEquals("XXX", realResp.Response.get(0).getTitle());
            }

            @Override
            public void failed(String managerId, String messageId, Object data) {

            }
        });
    }

}
