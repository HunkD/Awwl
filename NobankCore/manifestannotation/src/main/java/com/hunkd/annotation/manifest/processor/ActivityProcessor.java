package com.hunkd.annotation.manifest.processor;

import com.hunkd.annotation.manifest.model.Activity;
import com.hunkd.annotation.manifest.model.IntentFilters;
import com.hunkd.annotation.manifest.model.Manifest;
import com.hunkd.annotation.manifest.model.UsePermission;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class ActivityProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        // collect elements and format them with data structure.
        for (TypeElement te : annotations) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Printing: annotation = " + te.toString());
            for (Element e : roundEnv.getElementsAnnotatedWith(te)) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Printing: element use this annotation = " + e.toString());
            }
        }
        // print elements with XML format.
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotationTypes = new LinkedHashSet<>();
        supportedAnnotationTypes.add(Activity.class.getCanonicalName());
        supportedAnnotationTypes.add(Manifest.class.getCanonicalName());
        supportedAnnotationTypes.add(IntentFilters.class.getCanonicalName());
        supportedAnnotationTypes.add(UsePermission.class.getCanonicalName());
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
