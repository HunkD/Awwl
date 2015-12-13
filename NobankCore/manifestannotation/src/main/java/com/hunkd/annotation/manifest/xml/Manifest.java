package com.hunkd.annotation.manifest.xml;

import com.hunkd.annotation.manifest.xml.elements.Application;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Manifest {
    String packageName;

    @XmlElement
    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    Application application;

    @XmlAttribute(name="package")
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

}
