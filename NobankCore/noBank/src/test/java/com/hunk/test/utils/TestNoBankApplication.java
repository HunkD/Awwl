package com.hunk.test.utils;

import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.util.Hunk;

import org.robolectric.TestLifecycleApplication;
import org.robolectric.util.ReflectionHelpers;

import java.lang.reflect.Method;

public class TestNoBankApplication extends NoBankApplication implements TestLifecycleApplication {

    @Override
    public void onCreate() {
        ReflectionHelpers.setStaticField(Hunk.class, "mMockFlag", Boolean.TRUE);
        Hunk.getSingInfo(null);

        super.onCreate();
    }

    @Override
    public void beforeTest(Method method) {
        mockHunkInfo();
    }

    @Override
    public void prepareTest(Object test) {

    }

    @Override
    public void afterTest(Method method) {

    }

    private void mockHunkInfo() {

    }
}
