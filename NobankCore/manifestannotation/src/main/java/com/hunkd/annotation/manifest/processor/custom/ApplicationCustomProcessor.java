package com.hunkd.annotation.manifest.processor.custom;

import com.hunkd.annotation.manifest.model.AActivity;
import com.hunkd.annotation.manifest.model.AApplication;
import com.hunkd.annotation.manifest.processor.CustomProcessor;
import com.hunkd.annotation.manifest.processor.ProcessorFactory;
import com.hunkd.annotation.manifest.xml.elements.Activity;
import com.hunkd.annotation.manifest.xml.elements.Application;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class ApplicationCustomProcessor extends CustomProcessor <Application, AApplication> {

    public ApplicationCustomProcessor(ProcessorFactory processorFactory) {
        super(processorFactory);
    }

    @Override
    public Application process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv,
                        Messager messager) {
        for (Element e : roundEnv.getElementsAnnotatedWith(AApplication.class)) {
            AApplication aApplication = e.getAnnotation(AApplication.class);

            Application application = new Application();
            application.setLabel(aApplication.label());
            application.setBackup(aApplication.allowBackup());
            application.setIcon(aApplication.icon());
            // find Activity
            application.setActivities(findActivities(annotations, roundEnv, messager));
            return application;
        }
        return null;
    }

    private List<Activity> findActivities(Set<? extends TypeElement> annotations,
                                          RoundEnvironment roundEnv, Messager messager) {
        CustomProcessor activityCustomProcessor =
                mProcessorFactory.getCustomProcessor(AActivity.class);
        return (List<Activity>) activityCustomProcessor.process(annotations, roundEnv, messager);
    }

    @Override
    public Class<AApplication> getAnnotation() {
        return AApplication.class;
    }
}
