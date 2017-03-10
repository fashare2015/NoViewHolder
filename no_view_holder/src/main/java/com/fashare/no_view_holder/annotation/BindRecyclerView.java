package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

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

    class Behavior extends IBehavior.Simple<BindRecyclerView, RecyclerView, List>{
        public Behavior() {
            super(BindRecyclerView.class, List.class);
        }

        @Override
        protected int getId(BindRecyclerView annotation) {
            return annotation.id();
        }

        @Override
        public void onBind(RecyclerView targetView, BindRecyclerView annotation, List value) {
            RecyclerView.Adapter adapter = targetView.getAdapter();
            if(adapter == null) {
                LayoutManager lm = annotation.layoutManager();
                targetView.setLayoutManager(lm.style().get(targetView.getContext(), lm.spanCount()));
                targetView.setAdapter(new NoRecyclerViewAdapter(targetView.getContext(), annotation.layout(), value));
            }else if(adapter instanceof NoRecyclerViewAdapter)
                ((NoRecyclerViewAdapter) targetView.getAdapter()).setDataList(value);
        }
    }
}
