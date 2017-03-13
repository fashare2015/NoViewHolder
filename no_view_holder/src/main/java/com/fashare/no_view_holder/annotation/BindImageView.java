package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.widget.ImageView;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.ImageLoader;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 16-11-19.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindImageView {
    int NO_DRAWABLE = -1;

    @IdRes int id();
    int placeHolder() default NO_DRAWABLE;

    class Behavior extends IBehavior.Simple<BindImageView, ImageView, String>{
        public Behavior() {
            super(BindImageView.class, String.class);
        }

        @Override
        protected int getId(BindImageView annotation) {
            return annotation.id();
        }

        @Override
        protected void onBind(ImageView targetView, BindImageView annotation, String value) {
            ImageLoader.loadImage(targetView, value, annotation.placeHolder());
        }
    }
}
