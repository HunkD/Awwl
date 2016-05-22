package com.hunk.test.utils;

import com.hunk.nobank.Core;
import com.hunk.nobank.manager.UserManager;

import org.mockito.internal.util.MockUtil;

import static org.mockito.Mockito.mock;

/**
 * @author HunkDeng
 * @since 2016/5/22
 */
public class MockCore {
    static final MockUtil MOCK_UTIL = new MockUtil();

    public static UserManager mockUserManager() {
        UserManager srcUserManager = Core.getInstance().getUserManager();
        if (srcUserManager != null && MOCK_UTIL.isMock(srcUserManager)) {
            return srcUserManager;
        } else {
            UserManager mockUserManager = mock(UserManager.class);
            Core.getInstance().setLoginManager(mockUserManager);
            return mockUserManager;
        }
    }
}
