package com.hunk.astub;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author HunkDeng
 * @since 2016/6/19
 */
public class FileUtil {
    /**
     * Copy one folder
     * @param context
     * @param imgPath
     * @param targetPath
     * @return
     */
    public static boolean copyAssetFolder(Context context, String imgPath, String targetPath) {
        AssetManager assetManager = context.getAssets();
        try {
            String[] fileNames = assetManager.list(imgPath);
            if (fileNames.length != 0) {
                File file = new File(targetPath);
                // create folder
                if (file.mkdirs()) {
                    //
                    for (String fileName : fileNames) {
                        copyAssetFile(assetManager, imgPath + "/" + fileName, targetPath + fileName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Copy one file path from Asset to target file path
     * @param assetManager
     * @param from
     * @param to
     * @throws IOException
     */
    private static void copyAssetFile(AssetManager assetManager, String from, String to) throws IOException {
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            myOutput = new FileOutputStream(to);
            myInput = assetManager.open(from);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }
            myOutput.flush();
        } finally {
            if (myInput != null) {
                try {
                    myInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (myOutput != null) {
                try {
                    myOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String encodeBase64File(String path) throws IOException {
        return Base64.encodeToString(getFileByte(path), Base64.DEFAULT);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static byte[] getFileByte(String path) throws IOException {
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        BufferedInputStream buf = null;
        try {
            buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
        } finally {
            if (buf != null) {
                try {
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }
}
