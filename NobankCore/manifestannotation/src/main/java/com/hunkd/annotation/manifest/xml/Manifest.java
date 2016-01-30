package com.hunkd.annotation.manifest.xml;

import com.hunkd.annotation.manifest.xml.elements.Application;
import com.hunkd.annotation.manifest.xml.elements.UsePermission;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Manifest {
    private String packageName;
    private Application application;
    private List<UsePermission> usePermissions = new ArrayList<>();

    @XmlAttribute(name="package")
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @XmlElement(name = "uses-permission")
    public List<UsePermission> getUsePermissions() {
        return usePermissions;
    }

    public void setUsePermissions(List<UsePermission> usePermissions) {
        this.usePermissions = usePermissions;
    }

    public void addUsePermissions(UsePermission userPermission) {
        if (usePermissions != null) {
            usePermissions.add(userPermission);
        }
    }

    @XmlElement
    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
