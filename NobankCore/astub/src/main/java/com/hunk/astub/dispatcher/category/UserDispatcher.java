package com.hunk.astub.dispatcher.category;

import com.hunk.astub.dispatcher.method.Method;
import com.hunk.astub.dispatcher.method.LoginMethodHandler;

/**
 *
 */
public class UserDispatcher extends NormalCategoryDispatcher {

    public UserDispatcher() {
        // mapping here!
        addMethodHandler(Method.Login.toString(), new LoginMethodHandler());
    }
}
