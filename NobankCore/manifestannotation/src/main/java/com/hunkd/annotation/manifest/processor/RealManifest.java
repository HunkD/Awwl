package com.hunkd.annotation.manifest.processor;

import com.hunkd.annotation.manifest.xml.Manifest;

import java.util.HashMap;
import java.util.Map;

/**
 * Keep real manifest which will deserialize to xml.
 * Each element may be a stub element when serializing, so we will hold an extra property to keep record on that.
 */
public class RealManifest {
    // Real manifest
    Manifest mManifest;
    //
    Map<String, Object> objectMap = new HashMap<>();
    //
    private String mLocation;

    public Manifest getManifest() {
        return mManifest;
    }

    public void setManifest(Manifest manifest) {
        this.mManifest = manifest;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public String getLocation() {
        return mLocation;
    }
}
