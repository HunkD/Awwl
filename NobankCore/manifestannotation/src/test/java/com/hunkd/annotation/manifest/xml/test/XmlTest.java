package com.hunkd.annotation.manifest.xml.test;

import com.hunkd.annotation.manifest.xml.elements.Application;
import com.hunkd.annotation.manifest.xml.Manifest;

import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBException;

public class XmlTest {
    @Test
    public void genPackageName() throws JAXBException {
        Manifest manifest = new Manifest();
        manifest.setPackageName("PACKAGE_NAME");

        String result = Util.genXmlStr(manifest);
        String expected =
                "<manifest package=\"PACKAGE_NAME\" xmlns:android=\"http://schemas.android.com/apk/res/android\"/>";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void genNameSpace() throws JAXBException {
        Manifest manifest = new Manifest();

        String result = Util.genXmlStr(manifest);
        String expected =
                "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"/>";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void genApplication() throws JAXBException {
        Manifest manifest = new Manifest();
        manifest.setApplication(new Application());

        String result = Util.genXmlStr(manifest);
        String expected =
                "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                        "    <application/>\n" +
                        "</manifest>";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void genBackup() throws JAXBException {
        Manifest manifest = new Manifest();
        Application application = new Application();
        application.setBackup(true);
        manifest.setApplication(application);

        String result = Util.genXmlStr(manifest);
        String expected =
                "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                        "    <application android:backup=\"true\"/>\n" +
                        "</manifest>";
        Assert.assertEquals(expected, result);
    }
}
