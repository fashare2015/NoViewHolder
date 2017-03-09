package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by apple on 16-11-19.
 */
@Target(FIELD)
@Retention(CLASS)
public @interface BindImageView {
    int NO_DRAWABLE = -1;

    @IdRes int id() default View.NO_ID;
    int placeHolder() default NO_DRAWABLE;
}
