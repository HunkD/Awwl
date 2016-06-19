package com.hunk.test.utils;

import com.hunk.nobank.Core;
import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.util.Hunk;

import org.robolectric.TestLifecycleApplication;
import org.robolectric.util.ReflectionHelpers;

import java.lang.reflect.Method;

/**
 * Test Application
 * which can add test operation before and after for each application method.
 */
public class TestNoBankApplication extends NoBankApplication implements TestLifecycleApplication {

    NetworkHandlerStub networkHandlerStub;

    @Override
    public void onCreate() {
        ReflectionHelpers.setStaticField(Hunk.class, "mMockFlag", Boolean.TRUE);
        Hunk.getSingInfo(null);

        super.onCreate();

        networkHandlerStub = new NetworkHandlerStub(this.getApplicationContext());
        Core.getInstance().setNetworkHandler(networkHandlerStub);
    }

    @Override
    public void beforeTest(Method method) {

    }

    @Override
    public void prepareTest(Object test) {

    }

    @Override
    public void afterTest(Method method) {

    }
}
