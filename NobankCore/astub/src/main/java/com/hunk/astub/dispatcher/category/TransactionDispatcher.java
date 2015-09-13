package com.hunk.astub.dispatcher.category;

import com.hunk.astub.dispatcher.method.Method;
import com.hunk.astub.dispatcher.method.SummaryMethodHandler;
import com.hunk.astub.dispatcher.method.TransListMethodHandler;

/**
 *
 */
public class TransactionDispatcher extends NormalCategoryDispatcher {
    public TransactionDispatcher() {
        addMethodHandler(Method.List.toString(), new TransListMethodHandler());
    }
}
