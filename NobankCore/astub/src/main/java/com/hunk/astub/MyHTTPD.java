package com.hunk.astub;

import fi.iki.elonen.NanoHTTPD;

public class MyHTTPD extends NanoHTTPD {

    public MyHTTPD(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String resp = "{\"Code\":0,\"Response\":{\"NeedSecurityQuestionCheck\":true,\"AllAccountIds\":[\"123\",\"456\"]}}";
        Method httpMethod = session.getMethod();
        if (httpMethod == Method.GET) {

        } else if (httpMethod == Method.POST) {

        }
        return newFixedLengthResponse(resp);
    }

    private Response newFixedLengthResponse(String resp) {
        return new Response(Response.Status.OK, MIME_PLAINTEXT, resp);
    }
}
