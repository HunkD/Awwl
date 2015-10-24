package com.hunk.test.nobank.networkTest;

import android.os.Build;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 *
 */
@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class CacheTest {

    @Test
    public void testCacheShouldFetch() {
        AccountSummaryPackage pack = new AccountSummaryPackage();
        pack.setCache(new RealResp(), pack);
        AccountSummaryPackage packAnother = new AccountSummaryPackage();
        Assert.assertFalse(AccountSummaryPackage.cache.shouldFetch(packAnother));
    }

    @Test
    public void testCacheExpire() {
        AccountSummaryPackage pack = new AccountSummaryPackage();
        pack.setCache(new RealResp(), pack);
        AccountSummaryPackage packAnother = new AccountSummaryPackage();
        AccountSummaryPackage.cache.expire();

        Assert.assertTrue(AccountSummaryPackage.cache.shouldFetch(packAnother));
    }

    @Test
    public void testCacheGet() {
        RealResp realResp = new RealResp();
        AccountSummaryPackage pack = new AccountSummaryPackage();
        pack.setCache(realResp, pack);
        AccountSummaryPackage packAnother = new AccountSummaryPackage();

        Assert.assertFalse(AccountSummaryPackage.cache.shouldFetch(packAnother));
        Assert.assertEquals(realResp, AccountSummaryPackage.cache.get());
    }
}
