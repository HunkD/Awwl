package com.hunkd.annotation.manifest.processor.custom;

import com.hunkd.annotation.manifest.model.AUsePermission;
import com.hunkd.annotation.manifest.processor.CustomProcessor;
import com.hunkd.annotation.manifest.processor.ProcessorFactory;
import com.hunkd.annotation.manifest.xml.elements.UsePermission;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class UsePermissionCustomProcessor extends CustomProcessor<List<UsePermission>, AUsePermission> {

    public UsePermissionCustomProcessor(ProcessorFactory processorFactory) {
        super(processorFactory);
    }

    @Override
    public List<UsePermission> process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv, Messager messager) {
        List<UsePermission> usePermissions = new ArrayList<>();
        for (Element e : roundEnv.getElementsAnnotatedWith(AUsePermission.class)) {
            AUsePermission aUsePermission = e.getAnnotation(AUsePermission.class);
            for (String usePermissionStr : aUsePermission.name()) {
                UsePermission usePermission = new UsePermission(usePermissionStr);
                usePermissions.add(usePermission);
            }
        }
        return usePermissions;
    }

    @Override
    public Class<AUsePermission> getAnnotation() {
        return AUsePermission.class;
    }
}
