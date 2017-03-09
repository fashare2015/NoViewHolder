package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.widget.NoRecyclerViewAdapter;

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
public @interface BindRecyclerView {
    @IdRes int id();

    @LayoutRes int layout();

    LayoutManager layoutManager() default @LayoutManager;

    class Behavior extends IBehavior.Simple<BindRecyclerView, List>{
        public Behavior() {
            super(BindRecyclerView.class, List.class);
        }

        @Override
        public void onBind(View itemView, final BindRecyclerView annotation, final List value) {
            final RecyclerView rv = (RecyclerView) itemView.findViewById(annotation.id());
            bindIfNotNull(rv, annotation.id(), new Runnable() {
                @Override
                public void run() {
                    LayoutManager lm = annotation.layoutManager();
                    rv.setLayoutManager(lm.style().get(rv.getContext(), lm.spanCount()));
                    rv.setAdapter(new NoRecyclerViewAdapter(rv.getContext(), annotation.layout(), value));
                }
            });
        }
    }
}
