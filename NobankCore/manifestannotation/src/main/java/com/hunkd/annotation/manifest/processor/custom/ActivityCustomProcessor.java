package com.hunkd.annotation.manifest.processor.custom;

import com.hunkd.annotation.manifest.model.AActivity;
import com.hunkd.annotation.manifest.model.AIntentFilters;
import com.hunkd.annotation.manifest.model.AUsePermission;
import com.hunkd.annotation.manifest.processor.CustomProcessor;
import com.hunkd.annotation.manifest.processor.ProcessorFactory;
import com.hunkd.annotation.manifest.xml.elements.Action;
import com.hunkd.annotation.manifest.xml.elements.Activity;
import com.hunkd.annotation.manifest.xml.elements.Category;
import com.hunkd.annotation.manifest.xml.elements.IntentFilter;
import com.hunkd.annotation.manifest.xml.elements.UsePermission;

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

            findRelatedAnnotation(activity, e, roundEnv, messager);
        }
        return activities;
    }

    private void findRelatedAnnotation(Activity activity, Element element,
                                       RoundEnvironment roundEnv, Messager messager) {
        // parse permissions
        AUsePermission permission = element.getAnnotation(AUsePermission.class);
        if (permission != null) {
            String[] permissions = permission.name();
            for (String permissionStr : permissions) {
                activity.addUsePermissions(new UsePermission(permissionStr));
            }
        }
        // parse intent filters
        AIntentFilters intentFilters = element.getAnnotation(AIntentFilters.class);
        IntentFilter intentFilter = new IntentFilter();
        if (intentFilters != null) {
            String[] actions = intentFilters.actions();
            for (String action : actions) {
                intentFilter.addAction(new Action(action));
            }
            String[] categories = intentFilters.categories();
            for(String category : categories) {
                intentFilter.addCategory(new Category(category));
            }
        }
        activity.setIntentFilter(intentFilter);
    }

    @Override
    public Class<AActivity> getAnnotation() {
        return AActivity.class;
    }
}
