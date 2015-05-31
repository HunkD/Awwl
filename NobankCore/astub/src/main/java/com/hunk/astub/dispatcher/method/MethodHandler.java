package com.hunk.astub.dispatcher.method;

import fi.iki.elonen.NanoHTTPD;

/**
 *
 */
public interface MethodHandler {
    String handle(NanoHTTPD.IHTTPSession session);
}
