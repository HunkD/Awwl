package com.hunk.test.nobank.networkTest;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 *
 */
@RunWith(RobolectricTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class CacheTest {

    @Test
    public void testCacheShouldFetch() {
        AccountSummaryPackage pack = new AccountSummaryPackage();
        pack.setCache(new AccountSummary(), pack);
        AccountSummaryPackage packAnother = new AccountSummaryPackage();
        Assert.assertFalse(AccountSummaryPackage.cache.shouldFetch(packAnother));
    }

    @Test
    public void testCacheExpire() {
        AccountSummaryPackage pack = new AccountSummaryPackage();
        pack.setCache(new AccountSummary(), pack);
        AccountSummaryPackage packAnother = new AccountSummaryPackage();
        AccountSummaryPackage.cache.expire();

        Assert.assertTrue(AccountSummaryPackage.cache.shouldFetch(packAnother));
    }

    @Test
    public void testCacheGet() {
        AccountSummary accountSummary = new AccountSummary();
        AccountSummaryPackage pack = new AccountSummaryPackage();
        pack.setCache(accountSummary, pack);
        AccountSummaryPackage packAnother = new AccountSummaryPackage();

        Assert.assertFalse(AccountSummaryPackage.cache.shouldFetch(packAnother));
        Assert.assertEquals(accountSummary, AccountSummaryPackage.cache.get());
    }
}
