package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.TextView;

import com.fashare.no_view_holder.IBehavior;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 16-11-19.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindTextView {
    @IdRes int id();

    class Behavior extends IBehavior.Simple<BindTextView, String>{
        public Behavior() {
            super(BindTextView.class, String.class);
        }

        @Override
        public void onBind(View itemView, BindTextView annotation, final String value) {
            final TextView tv = (TextView) itemView.findViewById(annotation.id());
            bindIfNotNull(tv, annotation.id(), new Runnable() {
                @Override
                public void run() {
                    tv.setText(value);
                }
            });
        }
    }
}
