package com.hunkd.annotation.manifest.xml.elements;

import javax.xml.bind.annotation.XmlAttribute;

public class UsePermission {
    String name;

    public UsePermission() {
    }

    public UsePermission(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
