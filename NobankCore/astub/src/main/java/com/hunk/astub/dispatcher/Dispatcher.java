package com.hunk.astub.dispatcher;

import fi.iki.elonen.NanoHTTPD;

/** Dispatcher request interface definition
 */
public interface Dispatcher {
    /**
     * Dispatch session to handler
     * @param session
     * @return
     */
    String dispatch(NanoHTTPD.IHTTPSession session);
}
