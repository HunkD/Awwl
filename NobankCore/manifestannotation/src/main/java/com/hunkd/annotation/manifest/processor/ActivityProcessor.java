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
        messager.printMessage(Diagnostic.Kind.NOTE, "Printing: get invoke process method.");
        // collect elements and format them with data structure.
        for (Element e : roundEnv.getElementsAnnotatedWith(Activity.class)) {
            Activity annotation = e.getAnnotation(Activity.class);
            messager.printMessage(Diagnostic.Kind.NOTE, "Printing: field 'name' of Activity = " + annotation.name());
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(Manifest.class)) {
            Manifest annotation = e.getAnnotation(Manifest.class);
            messager.printMessage(Diagnostic.Kind.NOTE, "Printing: field 'name' of Manifest = " + annotation.packageName());
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(IntentFilters.class)) {
            IntentFilters annotation = e.getAnnotation(IntentFilters.class);
            messager.printMessage(Diagnostic.Kind.NOTE, "Printing: field 'name' of IntentFilters = " + annotation.actions()[0]);
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(UsePermission.class)) {
            UsePermission annotation = e.getAnnotation(UsePermission.class);
            messager.printMessage(Diagnostic.Kind.NOTE, "Printing: field 'name' of UsePermission = " + annotation.name()[0]);
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
