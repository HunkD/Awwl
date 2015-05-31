package com.hunk.astub.dispatcher.category;

import android.net.Uri;

import com.hunk.astub.dispatcher.Dispatcher;

import java.util.HashMap;

import fi.iki.elonen.NanoHTTPD;

public class CategoryDispatcher implements Dispatcher {

    private HashMap<String, Dispatcher> categoryDispatcherMap;

    public CategoryDispatcher() {
        this.categoryDispatcherMap = new HashMap<>();
        // mapping here!
        categoryDispatcherMap.put(Category.User.toString(), new UserDispatcher());
        categoryDispatcherMap.put(Category.Account.toString(), new AccountDispatcher());
    }

    public String dispatch(NanoHTTPD.IHTTPSession session) {
        Uri uri = Uri.parse(session.getUri());
        String category = uri.getPathSegments().get(0);
        return categoryDispatcherMap.get(category).dispatch(session);
    }
}