package com.hunk.nobank.extension.network.interfaces;

import java.util.ArrayList;
import java.util.Collection;

public class SequenceRequest {
    Collection<RequestHandler> handlers = new ArrayList<RequestHandler>();

    public boolean execute() {
        for (RequestHandler handler : handlers) {
            BaseResponse<?> baseResp = handler.sendRequest();
            if (baseResp.isSuccess) {

            } else {
                return false;
            }
        }
        return true;
    }

    public void addRequestHandler(RequestHandler generate) {
        handlers.add(generate);
    }
}
