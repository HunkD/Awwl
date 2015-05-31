package com.hunk.astub;

import com.hunk.astub.dispatcher.category.CategoryDispatcher;

import java.util.Random;

import fi.iki.elonen.NanoHTTPD;

public class MyHTTPD extends NanoHTTPD {

    private final Random mRandom;
    private CategoryDispatcher mCategoryDispatcher;

    public MyHTTPD(int port) {
        super(port);
        mCategoryDispatcher = new CategoryDispatcher();
        mRandom = new Random();
    }

    @Override
    public Response serve(IHTTPSession session) {
        waitForRandomTime();
        String resp = mCategoryDispatcher.dispatch(session);

        return newFixedLengthResponse(resp);
    }

    private void waitForRandomTime() {
        int n = genRandomInteger(2, 6, mRandom);
        try {
            Thread.sleep(n * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int genRandomInteger(int aStart, int aEnd, Random aRandom){
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long)aEnd - (long)aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
        return (int)(fraction + aStart);
    }

    private Response newFixedLengthResponse(String resp) {
        return new Response(Response.Status.OK, MIME_PLAINTEXT, resp);
    }
}
