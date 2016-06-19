package com.hunk.astub.dispatcher.category;

import com.hunk.astub.dispatcher.method.ImgLoadMethodHandler;
import com.hunk.astub.dispatcher.method.Method;

/**
 * @author HunkDeng
 * @since 2016/6/19
 */
public class ImageDispatcher extends NormalCategoryDispatcher {

    public ImageDispatcher() {
        addMethodHandler(Method.Load.toString(), new ImgLoadMethodHandler());
    }
}
