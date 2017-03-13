package com.fashare.no_view_holder.annotation;

import android.view.View;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.NoViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 16-11-19.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindImageViews {
    BindImageView[] value();

    class Behavior extends IBehavior.Group<BindImageViews, List> {
        public Behavior() {
            super(BindImageViews.class, List.class);
        }

        @Override
        protected void onBind(NoViewHolder noViewHolder, View itemView, BindImageViews annotation, List value) {
            IBehavior.Simple behavior = new BindImageView.Behavior();
            for (int i=0; i<annotation.value().length && i<value.size(); i++) {
                BindImageView innerAnnotation = annotation.value()[i];
                behavior.bindIfNotNull(noViewHolder, itemView.findViewById(innerAnnotation.id()), innerAnnotation, value.get(i), null);
            }
        }
    }
}
