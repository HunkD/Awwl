package com.hunkd.annotation.manifest.xml.elements;

import javax.xml.bind.annotation.XmlAttribute;

public class Action {
    @XmlAttribute
    String name;

    public Action(String action) {
        name = action;
    }

    public Action() {

    }
}
