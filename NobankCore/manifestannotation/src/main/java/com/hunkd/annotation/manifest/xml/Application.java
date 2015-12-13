package com.hunkd.annotation.manifest.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class Application {
    Boolean backup;

    @XmlAttribute(namespace = "http://schemas.android.com/apk/res/android")
    public Boolean isBackup() {
        return backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }
}
