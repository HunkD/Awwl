package com.hunkd.annotation.manifest.processor;

import com.hunkd.annotation.manifest.model.AManifest;
import com.hunkd.annotation.manifest.processor.custom.ManifestCustomProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class ManifestProcessor extends AbstractProcessor {

    private ProcessorFactory mProcessorFactory;

    private RealManifest mRealManifest = new RealManifest();
    private Types mTypeUtils;
    private Elements mTelementUtils;
    private Filer mFiler;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mTypeUtils = processingEnv.getTypeUtils();
        mTelementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();

        mProcessorFactory = new ProcessorFactory();
        mProcessorFactory.setFiler(mFiler);
        mProcessorFactory.setMessager(mMessager);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Printing: get invoke process method.");
        // setup
        if (!mProcessorFactory.hasSetup()) {
            mProcessorFactory.setup(mRealManifest);
            messager.printMessage(Diagnostic.Kind.NOTE, "Printing: setup.");
        }
        // collect elements and format them with data structure.
        ManifestCustomProcessor manifestCustomProcessor =
                (ManifestCustomProcessor) mProcessorFactory.getCustomProcessor(AManifest.class);
        manifestCustomProcessor.process(annotations, roundEnv, messager);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        if (!mProcessorFactory.hasSetup()) {
            mProcessorFactory.setup(mRealManifest);
        }

        Set<String> supportedAnnotationTypes = new LinkedHashSet<>();
        for (CustomProcessor customProcessor : mProcessorFactory.getAllCustomProcessors()) {
            supportedAnnotationTypes.add(customProcessor.getAnnotation().getCanonicalName());
        }
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
