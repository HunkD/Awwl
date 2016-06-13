package com.hunk.astub.dispatcher.category;

import android.net.Uri;

import com.hunk.astub.dispatcher.Dispatcher;

import java.util.HashMap;

import fi.iki.elonen.NanoHTTPD;

/**
 * First level dispatcher to handle category part
 */
public class CategoryDispatcher implements Dispatcher {

    private HashMap<String, Dispatcher> categoryDispatcherMap;

    public CategoryDispatcher() {
        this.categoryDispatcherMap = new HashMap<>();
        // mapping here!
        categoryDispatcherMap.put(Category.User.toString(), new UserDispatcher());
        categoryDispatcherMap.put(Category.Account.toString(), new AccountDispatcher());
        categoryDispatcherMap.put(Category.Transaction.toString(), new TransactionDispatcher());
    }

    public String dispatch(NanoHTTPD.IHTTPSession session) {
        Uri uri = Uri.parse(session.getUri());
        // API format Category/Method
        String category = uri.getPathSegments().get(0);
        // Pass this session to specific Category dispatcher
        return categoryDispatcherMap.get(category).dispatch(session);
    }
}
