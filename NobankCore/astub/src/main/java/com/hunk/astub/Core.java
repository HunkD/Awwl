package com.hunk.astub;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.File;

/**
 * @author HunkDeng
 * @since 2016/6/19
 */
public class Core {
    private static final String IMG_PATH = "images";
    private static Core instance = new Core();
    private String mImgFolderPath;

    private Core() {}
    public static synchronized Core getInstance() {
        return instance;
    }

    public void copyImgFolder(Context context) {
        String targetPath = context.getFilesDir() + "/" + IMG_PATH + "/";
        // if exist this folder, we think it has been done last time.
        if (new File(targetPath).exists()) {
            mImgFolderPath = targetPath;
            return;
        }
        if (FileUtil.copyAssetFolder(context, IMG_PATH, targetPath)) {
            mImgFolderPath = targetPath;
        }
    }

    @Nullable
    public String getImgFolderPath() {
        return mImgFolderPath;
    }
}
