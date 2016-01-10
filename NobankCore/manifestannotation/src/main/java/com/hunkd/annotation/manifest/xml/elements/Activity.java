package com.hunkd.annotation.manifest.xml.elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Activity {
    private String name;
    private List<UsePermission> usePermissions = new ArrayList<>();
    private IntentFilter intentFilter;

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UsePermission> getUsePermissions() {
        return usePermissions;
    }

    public void addUsePermissions(UsePermission usePermission) {
        usePermissions.add(usePermission);
    }

    @XmlElement(name = "intent-filter")
    public IntentFilter getIntentFilter() {
        return intentFilter;
    }

    public void setIntentFilter(IntentFilter intentFilter) {
        this.intentFilter = intentFilter;
    }
}
