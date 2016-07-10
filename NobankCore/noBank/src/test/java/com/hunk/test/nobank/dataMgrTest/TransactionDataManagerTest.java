package com.hunk.test.nobank.dataMgrTest;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.contract.TransactionType;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.manager.TransactionListCache;
import com.hunk.nobank.model.Cache;
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
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class TransactionDataManagerTest {

    private NetworkHandlerStub mNetworkHandlerStub;

    @Before
    public void prepare() {
        // set next response
        mNetworkHandlerStub =
                (NetworkHandlerStub) Core.getInstance().getNetworkHandler();

        mNetworkHandlerStub.clear();

        TransactionReqPackage.cache.expire();
    }

    @Test
    public void testFetchTransactionWithoutCache() {
        TransactionDataManager transactionDataManager = new TransactionDataManager(null);

        TransactionFields transactionFields = new TransactionFields("XXX", 1000, TransactionType.DEPOSIT, 1000);
        RealResp<List<TransactionFields>> realResp = new RealResp<>();
        realResp.Response = new ArrayList<>();
        realResp.Response.add(transactionFields);

        mNetworkHandlerStub.setNextResponse(realResp);

        transactionDataManager
                .fetchTransactions(false)
                .subscribe(new Action1<List<TransactionFields>>() {
                    @Override
                    public void call(List<TransactionFields> transactionFieldsList) {
                        Assert.assertEquals(1, transactionFieldsList.size());
                        Assert.assertEquals("XXX", transactionFieldsList.get(0).getTitle());
                    }
                });
    }

    @Test
    public void testFetchTransactionWithCache() {
        TransactionDataManager transactionDataManager = new TransactionDataManager(null);
        TransactionReqPackage.cache = mock(TransactionListCache.class);
        TransactionFields transactionFields = new TransactionFields("XXX", 1000, TransactionType.DEPOSIT, 1000);
        List<TransactionFields> cacheObj = new ArrayList<>();
        cacheObj.add(transactionFields);
        when(TransactionReqPackage.cache.get())
                .thenReturn(cacheObj);
        when(TransactionReqPackage.cache.shouldFetch(any(TransactionReqPackage.class)))
                .thenReturn(false);

        transactionDataManager
                .fetchTransactions(false)
                .subscribe(new Action1<List<TransactionFields>>() {
                    @Override
                    public void call(List<TransactionFields> transactionFieldsList) {
                        Assert.assertEquals(1, transactionFieldsList.size());
                        Assert.assertEquals("XXX", transactionFieldsList.get(0).getTitle());
                    }
                });

        TransactionReqPackage.cache = new TransactionListCache();
    }

    @Test
    public void testFetchMoreTransactionWithoutCache() {
        TransactionDataManager transactionDataManager = new TransactionDataManager(null);
        TransactionFields transactionFields = new TransactionFields("XXX", 1000, TransactionType.DEPOSIT, 1000);
        List<TransactionFields> cacheObj = new ArrayList<>();
        cacheObj.add(transactionFields);
        TransactionReqPackage.cache.setCache(
                cacheObj, new TransactionReqPackage(new AccountSummary(), new Date().getTime()));

        TransactionFields transactionFields2 = new TransactionFields("XXX2", 1000, TransactionType.DEPOSIT, 1000);
        RealResp<List<TransactionFields>> realResp2 = new RealResp<>();
        realResp2.Response = new ArrayList<>();
        realResp2.Response.add(transactionFields2);

        mNetworkHandlerStub.setNextResponse(realResp2);

        transactionDataManager
                .fetchTransactions(true)
                .subscribe(new Action1<List<TransactionFields>>() {
                    @Override
                    public void call(List<TransactionFields> transactionFieldsList) {
                        Assert.assertEquals(2, TransactionReqPackage.cache.get().size());
                        Assert.assertEquals("XXX", TransactionReqPackage.cache.get().get(0).getTitle());
                        Assert.assertEquals("XXX2", TransactionReqPackage.cache.get().get(1).getTitle());
                    }
                });
    }
}
