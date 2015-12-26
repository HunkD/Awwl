package com.hunkd.annotation.manifest.processor;

import com.hunkd.annotation.manifest.model.AManifest;
import com.hunkd.annotation.manifest.processor.custom.ManifestCustomProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class ManifestProcessor extends AbstractProcessor {

    private ProcessorFactory mProcessorFactory = new ProcessorFactory();

    private RealManifest mRealManifest = new RealManifest();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Printing: get invoke process method.");
        // setup
        if (!mProcessorFactory.hasSetup()) {
            mProcessorFactory.setup(mRealManifest);
        }
        // collect elements and format them with data structure.
        ManifestCustomProcessor manifestCustomProcessor =
                (ManifestCustomProcessor) mProcessorFactory.getCustomProcessor(AManifest.class);
        manifestCustomProcessor.process(annotations, roundEnv, messager);
        // print elements with XML format.

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
