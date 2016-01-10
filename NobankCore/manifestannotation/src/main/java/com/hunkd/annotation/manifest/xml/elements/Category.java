package com.hunkd.annotation.manifest.xml.elements;

import javax.xml.bind.annotation.XmlAttribute;

public class Category {
    private String name;

    public Category() {
    }

    public Category(String category) {
        name = category;
    }
    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
