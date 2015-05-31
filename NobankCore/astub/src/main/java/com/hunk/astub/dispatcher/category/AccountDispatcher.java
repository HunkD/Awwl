package com.hunk.astub.dispatcher.category;

import com.hunk.astub.dispatcher.method.Method;
import com.hunk.astub.dispatcher.method.SummaryMethodHandler;

/**
 *
 */
public class AccountDispatcher extends NormalCategoryDispatcher {
    public AccountDispatcher() {
        addMethodHandler(Method.Summary.toString(), new SummaryMethodHandler());
    }
}
