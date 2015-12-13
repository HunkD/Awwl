package com.hunkd.annotation.manifest.xml.elements;

import javax.xml.bind.annotation.XmlAttribute;

public class Application {
    Boolean backup;

    @XmlAttribute
    public Boolean isBackup() {
        return backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }
}
