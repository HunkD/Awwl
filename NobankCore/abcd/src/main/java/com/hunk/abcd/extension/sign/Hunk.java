package com.hunk.abcd.extension.sign;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.hunk.abcd.extension.log.Logging;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Get Sign Information for this application
 */
public class Hunk {
    /**
     * When we run Roboletric test, method getSingInfo() will throw err, so add mock flag to control it.
     *
     * @see #getSingInfo(Context)
     */
    private final static Boolean mMockFlag = Boolean.TRUE;

    /**
     * Request system service PackageManager.GET_SIGNATURES to get signature for this application.
     * And then pass it to parseSignature to get Certification information.
     *
     * @param context application context
     * @return A wrapper class to describe readable certification information.
     * @see #parseSignature(byte[])
     * @see Hunk.HunkInfo
     */
    public static HunkInfo getSingInfo(Context context) {
        if (mMockFlag) return null;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            return parseSignature(sign.toByteArray());
        } catch (PackageManager.NameNotFoundException | CertificateException | NoSuchAlgorithmException e) {
            Logging.e(e.getMessage());
        }
        return null;
    }

    /**
     * @param signature signature byte array.
     * @return A wrapper class to describe readable certification information.
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @see #getSingInfo(android.content.Context)
     * @see Hunk.HunkInfo
     */
    public static HunkInfo parseSignature(byte[] signature) throws CertificateException, NoSuchAlgorithmException {
        // get certification
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
        // get SHA1
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] publicKey = md.digest(cert.getEncoded());
        String sha1 = byte2HexFormatted(publicKey);
        // get MD5
        md = MessageDigest.getInstance("MD5");
        publicKey = md.digest(cert.getEncoded());
        String md5 = byte2HexFormatted(publicKey);
        String signNumber = cert.getSerialNumber().toString();

        return new HunkInfo(cert.getSigAlgName(), sha1, md5, signNumber, cert.getSubjectDN().toString());
    }

    /**
     * To format the publicKey byte array like the format which KeyTool shows.
     *
     * @param arr public key byte array.
     * @return
     */
    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }

    /**
     * A class to show information for a certification.<br>
     * Also we overwrite toString method to help you log out what cert your application currently use.
     */
    public static class HunkInfo {
        public final String signName;
        // SHA1
        public final String publicKeySHA1;
        // md5
        public final String publicKeyMD5;
        public final String signNumber;
        public final String subjectDN;

        public HunkInfo(String signName, String publicKeySHA1, String publicKeyMD5, String signNumber, String subjectDN) {
            this.signName = signName;
            this.publicKeySHA1 = publicKeySHA1;
            this.publicKeyMD5 = publicKeyMD5;
            this.signNumber = signNumber;
            this.subjectDN = subjectDN;
        }

        @Override
        public String toString() {
            return "HunkInfo{" +
                    "signName='" + signName + '\'' +
                    ", publicKeySHA1='" + publicKeySHA1 + '\'' +
                    ", publicKeyMD5='" + publicKeyMD5 + '\'' +
                    ", signNumber='" + signNumber + '\'' +
                    ", subjectDN='" + subjectDN + '\'' +
                    '}';
        }
    }
}
