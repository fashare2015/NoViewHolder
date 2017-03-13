package com.fashare.no_view_holder.annotation.click;

import android.support.annotation.IdRes;
import android.view.View;

import com.fashare.no_view_holder.IBehavior;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 17-3-10.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindClick {
    @IdRes int id();

    class Behavior extends IBehavior.Simple<BindClick, View, View.OnClickListener>{
        public Behavior() {
            super(BindClick.class, View.OnClickListener.class);
        }

        @Override
        protected int getId(BindClick annotation) {
            return annotation.id();
        }

        @Override
        protected void onInitView(View targetView, BindClick annotation, final View.OnClickListener value) {
            targetView.setOnClickListener(value);
        }
    }
}
