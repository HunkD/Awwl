package com.hunkd.annotation.manifest.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AApplication {

    boolean allowBackup() default true;

    String label() default "@string/app_name";

    String icon() default "@mipmap/ic_launcher";

    String theme() default "@style/AppTheme";
}
