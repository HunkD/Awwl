package com.hunkd.annotation.manifest.processor;

import com.hunkd.annotation.manifest.model.AActivity;
import com.hunkd.annotation.manifest.model.AApplication;
import com.hunkd.annotation.manifest.model.AManifest;
import com.hunkd.annotation.manifest.model.AUsePermission;
import com.hunkd.annotation.manifest.processor.custom.ActivityCustomProcessor;
import com.hunkd.annotation.manifest.processor.custom.ApplicationCustomProcessor;
import com.hunkd.annotation.manifest.processor.custom.ManifestCustomProcessor;
import com.hunkd.annotation.manifest.processor.custom.UsePermissionCustomProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;

public class ProcessorFactory {
    private boolean mHasSetup;
    private RealManifest mRealManifest;
    private Map<Class<?>, CustomProcessor<?, ?>> customProcessors = new HashMap<>();
    private Filer mFiler;
    private Messager mMessager;

    public boolean hasSetup() {
        return mHasSetup;
    }

    public void setup(RealManifest realManifest) {
        mRealManifest = realManifest;
        // setup
        customProcessors.put(AManifest.class, new ManifestCustomProcessor(this));
        customProcessors.put(AApplication.class, new ApplicationCustomProcessor(this));
        customProcessors.put(AActivity.class, new ActivityCustomProcessor(this));
        customProcessors.put(AUsePermission.class, new UsePermissionCustomProcessor(this));
        //
        mHasSetup = true;
    }

    public Iterable<? extends CustomProcessor> getAllCustomProcessors() {
        return customProcessors.values();
    }

    public <Obj, AnnotationObj> CustomProcessor<Obj, AnnotationObj> getCustomProcessor(
            Class<Obj> objClass, Class<AnnotationObj> annotationClass) {
        return (CustomProcessor<Obj, AnnotationObj>) customProcessors.get(annotationClass);
    }

    public <Obj, AnnotationObj> CustomProcessor<List<Obj>, AnnotationObj> getCustomProcessorReturnList(
            Class<Obj> objClass, Class<AnnotationObj> annotationClass) {
        return (CustomProcessor<List<Obj>, AnnotationObj>) customProcessors.get(annotationClass);
    }

    public void setFiler(Filer filer) {
        this.mFiler = filer;
    }

    public void setMessager(Messager messager) {
        this.mMessager = messager;
    }

    public RealManifest getRealManifest() {
        return mRealManifest;
    }

    public Filer getFiler() {
        return mFiler;
    }

    public Messager getMessager() {
        return mMessager;
    }
}
