package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.widget.TextView;

import com.fashare.no_view_holder.IBehavior;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 16-11-19.
 *
 * Binder for TextView
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindTextView {
    @IdRes int id();

    class Behavior extends IBehavior.Simple<BindTextView, TextView, Object>{
        public Behavior() {
            super(BindTextView.class, Object.class);
        }

        @Override
        protected int getId(BindTextView annotation) {
            return annotation.id();
        }

        @Override
        protected void onBind(TextView targetView, BindTextView annotation, Object value) {
            targetView.setText(String.valueOf(value));
        }
    }
}
