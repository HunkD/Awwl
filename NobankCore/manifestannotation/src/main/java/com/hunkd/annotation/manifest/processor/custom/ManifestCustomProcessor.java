package com.hunkd.annotation.manifest.processor.custom;

import com.hunkd.annotation.manifest.model.AApplication;
import com.hunkd.annotation.manifest.model.AManifest;
import com.hunkd.annotation.manifest.model.AUsePermission;
import com.hunkd.annotation.manifest.processor.CustomProcessor;
import com.hunkd.annotation.manifest.processor.ProcessorFactory;
import com.hunkd.annotation.manifest.utils.Util;
import com.hunkd.annotation.manifest.xml.Manifest;
import com.hunkd.annotation.manifest.xml.elements.Application;
import com.hunkd.annotation.manifest.xml.elements.UsePermission;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.bind.JAXBException;

public class ManifestCustomProcessor extends CustomProcessor<Manifest, AManifest> {

    public ManifestCustomProcessor(ProcessorFactory processorFactory) {
        super(processorFactory);
    }

    @Override
    public Manifest process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv, Messager messager) {
        for (Element e : roundEnv.getElementsAnnotatedWith(AManifest.class)) {
            AManifest annotation = e.getAnnotation(AManifest.class);
            Manifest manifest = new Manifest();
            manifest.setPackageName(annotation.packageName());

            // find Application
            manifest.setApplication(findApplication(annotations, roundEnv, messager));
            // find uses permission
            manifest.setUsePermissions(findUserPermission(annotations, roundEnv, messager));

            mProcessorFactory.getRealManifest().setManifest(manifest);
            // print
            printManifestXml();
        }
        return null;
    }

    private List<UsePermission> findUserPermission(Set<? extends TypeElement> annotations,
                                                   RoundEnvironment roundEnv, Messager messager) {
        CustomProcessor<List<UsePermission>, AUsePermission> usePermissionCustomProcessor =
                mProcessorFactory.getCustomProcessorReturnList(UsePermission.class, AUsePermission.class);
        return usePermissionCustomProcessor.process(annotations, roundEnv, messager);
    }

    private Application findApplication(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv, Messager messager) {
        CustomProcessor<Application, AApplication> applicationCustomProcessor =
                mProcessorFactory.getCustomProcessor(Application.class, AApplication.class);
        return applicationCustomProcessor.process(annotations, roundEnv, messager);
    }

    private void printManifestXml() {
        try {
            FileObject fileObject = mProcessorFactory.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "AndroidManifest.xml");
            URI uri = fileObject.toUri();
            String path = uri.getPath().replace("build/intermediates/classes/", "build/intermediates/manifests/full/");
            mProcessorFactory.getMessager().printMessage(Diagnostic.Kind.NOTE, "Printing: location " + path);

            Util.genXml(mProcessorFactory.getRealManifest().getManifest(), path);
        } catch (JAXBException | IOException e1) {
            e1.printStackTrace();
        }
    }

    public Class<AManifest> getAnnotation() {
        return AManifest.class;
    }
}
