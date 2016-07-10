package com.hunk.test.utils;

import com.hunk.nobank.Core;
import com.hunk.nobank.NoBankApplication;
import com.hunk.abcd.extension.sign.Hunk;

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
        Hunk.getSingInfo(null);

        super.onCreate();

        networkHandlerStub = new NetworkHandlerStub();
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
