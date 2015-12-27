package com.hunkd.annotation.manifest.processor.custom;

import com.hunkd.annotation.manifest.model.AActivity;
import com.hunkd.annotation.manifest.processor.CustomProcessor;
import com.hunkd.annotation.manifest.processor.ProcessorFactory;
import com.hunkd.annotation.manifest.xml.elements.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class ActivityCustomProcessor extends CustomProcessor<List<Activity>, AActivity> {

    public ActivityCustomProcessor(ProcessorFactory processorFactory) {
        super(processorFactory);
    }

    @Override
    public List<Activity> process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv,
                                  Messager messager) {
        List<Activity> activities = new ArrayList<>();
        for (Element e : roundEnv.getElementsAnnotatedWith(AActivity.class)) {
            AActivity aActivity = e.getAnnotation(AActivity.class);
            Activity activity = new Activity();
            activity.setName(aActivity.name());

            activities.add(activity);
        }
        return activities;
    }

    @Override
    public Class<AActivity> getAnnotation() {
        return AActivity.class;
    }
}
