package com.hunk.astub.dispatcher.category;

import android.net.Uri;

import com.hunk.astub.dispatcher.Dispatcher;
import com.hunk.astub.dispatcher.method.MethodHandler;

import java.util.HashMap;

import fi.iki.elonen.NanoHTTPD;

/**
 *
 */
public class NormalCategoryDispatcher implements Dispatcher {

    private HashMap<String, MethodHandler> methodDispatcherMap;

    public void addMethodHandler(String s, MethodHandler handler) {
        if (methodDispatcherMap == null) {
            methodDispatcherMap = new HashMap<>();
        }
        methodDispatcherMap.put(s, handler);
    }

    @Override
    public String dispatch(NanoHTTPD.IHTTPSession session) {
        Uri uri = Uri.parse(session.getUri());
        // find out which method
        String method = uri.getPathSegments().get(1);
        // Let Method handler to handle the request
        return methodDispatcherMap.get(method).handle(session);
    }
}
