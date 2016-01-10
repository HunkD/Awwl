package com.hunkd.annotation.manifest.xml.test;

import com.hunkd.annotation.manifest.utils.Util;
import com.hunkd.annotation.manifest.xml.elements.Action;
import com.hunkd.annotation.manifest.xml.elements.Activity;
import com.hunkd.annotation.manifest.xml.elements.Application;
import com.hunkd.annotation.manifest.xml.Manifest;
import com.hunkd.annotation.manifest.xml.elements.Category;
import com.hunkd.annotation.manifest.xml.elements.IntentFilter;

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

    @Test
    public void genIcon() throws JAXBException {
        Manifest manifest = new Manifest();
        Application application = new Application();
        application.setIcon("@mipmap/ic_launcher");
        manifest.setApplication(application);

        String result = Util.genXmlStr(manifest);
        String expected =
                "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                        "    <application android:icon=\"@mipmap/ic_launcher\"/>\n" +
                        "</manifest>";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void genTheme() throws JAXBException {
        Manifest manifest = new Manifest();
        Application application = new Application();
        application.setTheme("@style/AppTheme");
        manifest.setApplication(application);

        String result = Util.genXmlStr(manifest);
        String expected =
                "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                        "    <application android:theme=\"@style/AppTheme\"/>\n" +
                        "</manifest>";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void genActivities() throws JAXBException {
        Activity activity = new Activity();
        activity.setName("FirstActivity");
        Activity activity2 = new Activity();
        activity2.setName("SecondActivity");

        Application application = new Application();
        application.setBackup(true);
        application.getActivities().add(activity);
        application.getActivities().add(activity2);

        Manifest manifest = new Manifest();
        manifest.setApplication(application);

        String result = Util.genXmlStr(manifest);
        String expected =
               "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                       "    <application android:backup=\"true\">\n" +
                       "        <activity android:name=\"FirstActivity\"/>\n" +
                       "        <activity android:name=\"SecondActivity\"/>\n" +
                       "    </application>\n" +
                       "</manifest>";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void genIntentFilerInActivity() throws JAXBException {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(new Category("category1"));
        intentFilter.addCategory(new Category("category2"));

        intentFilter.addAction(new Action("action1"));
        intentFilter.addAction(new Action("action2"));

        Activity activity = new Activity();
        activity.setIntentFilter(intentFilter);

        Application application = new Application();
        application.setBackup(true);
        application.getActivities().add(activity);

        Manifest manifest = new Manifest();
        manifest.setApplication(application);

        String result = Util.genXmlStr(manifest);
        String expected =
                "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                        "    <application android:backup=\"true\">\n" +
                        "        <activity>\n" +
                        "            <intent-filter>\n" +
                        "                <action android:name=\"action1\"/>\n" +
                        "                <action android:name=\"action2\"/>\n" +
                        "                <category android:name=\"category1\"/>\n" +
                        "                <category android:name=\"category2\"/>\n" +
                        "            </intent-filter>\n" +
                        "        </activity>\n" +
                        "    </application>\n" +
                        "</manifest>";
        Assert.assertEquals(expected, result);
    }
}
