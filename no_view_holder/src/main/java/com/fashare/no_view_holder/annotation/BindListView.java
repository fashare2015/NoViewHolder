package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ListView;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.widget.NoListViewAdapter;

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
public @interface BindListView {
    @IdRes int id();

    @LayoutRes int layout();

    class Behavior extends IBehavior.Simple<BindListView, List>{
        public Behavior() {
            super(BindListView.class, List.class);
        }

        @Override
        public void onBind(View itemView, final BindListView annotation, final List value) {
            final ListView lv = (ListView) itemView.findViewById(annotation.id());
            bindIfNotNull(lv, annotation.id(), new Runnable() {
                @Override
                public void run() {
                    lv.setAdapter(new NoListViewAdapter<>(lv.getContext(), annotation.layout(), value));
                }
            });
        }
    }
}
