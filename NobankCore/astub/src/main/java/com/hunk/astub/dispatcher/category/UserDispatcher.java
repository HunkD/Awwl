package com.hunk.astub.dispatcher.category;

import android.net.Uri;

import com.hunk.astub.Method;
import com.hunk.astub.dispatcher.method.MethodHandler;
import com.hunk.astub.dispatcher.Dispatcher;
import com.hunk.astub.dispatcher.method.LoginMethodHandler;

import java.util.HashMap;

import fi.iki.elonen.NanoHTTPD;

/**
 *
 */
public class UserDispatcher implements Dispatcher {

    private HashMap<String, MethodHandler> methodDispatcherMap;

    public UserDispatcher() {
        this.methodDispatcherMap = new HashMap<>();
        // mapping here!
        methodDispatcherMap.put(Method.Login.toString(), new LoginMethodHandler());
    }

    public String dispatch(NanoHTTPD.IHTTPSession session) {
        Uri uri = Uri.parse(session.getUri());
        String method = uri.getPathSegments().get(1);
        return methodDispatcherMap.get(method).handle(session);
    }
}
