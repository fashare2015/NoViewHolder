package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.view.View;
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

    class Behavior extends IBehavior.Simple<BindImageView, String>{
        public Behavior() {
            super(BindImageView.class, String.class);
        }

        @Override
        public void onBind(View itemView, final BindImageView annotation, final String value) {
            final ImageView iv = (ImageView) itemView.findViewById(annotation.id());

            bindIfNotNull(iv, annotation.id(), new Runnable(){
                @Override
                public void run() {
                    ImageLoader.loadImage(iv, value, annotation.placeHolder());
                }
            });
        }
    }
}
