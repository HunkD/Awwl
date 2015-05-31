package com.hunk.astub.dispatcher;

import fi.iki.elonen.NanoHTTPD;

public interface Dispatcher {
    String dispatch(NanoHTTPD.IHTTPSession session);
}
