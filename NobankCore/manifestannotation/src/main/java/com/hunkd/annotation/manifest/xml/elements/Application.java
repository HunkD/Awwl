package com.hunkd.annotation.manifest.xml.elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Application {
    private Boolean backup;
    private String label;
    List<Activity> activities = new ArrayList<>();
    private String icon;
    private String theme;

    @XmlAttribute
    public Boolean isBackup() {
        return backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    @XmlAttribute
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlElement(name = "activity")
    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @XmlAttribute
    public String getIcon() {
        return icon;
    }

    @XmlAttribute
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
