package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.widget.rv.ItemTypeDelegate;
import com.fashare.no_view_holder.widget.rv.NoRvAdapter;

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

    int itemType() default Integer.MAX_VALUE;

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
        protected void onBind(RecyclerView targetView, BindRecyclerView annotation, List value) {
            RecyclerView.Adapter adapter = targetView.getAdapter();
            if(adapter == null) {
                LayoutManager lm = annotation.layoutManager();
                targetView.setLayoutManager(lm.style().get(targetView.getContext(), lm.spanCount()));
                NoRvAdapter noRvAdapter = new NoRvAdapter(targetView.getContext(), annotation.layout(), value);
                noRvAdapter.addItemViewDelegate(new ItemTypeDelegate(annotation.layout(), annotation.itemType(), noRvAdapter));
                for (Object data : value) {
                    noRvAdapter.putType(data, annotation.itemType());
                }
                targetView.setAdapter(noRvAdapter);

            }else if(adapter instanceof NoRvAdapter) {
                NoRvAdapter noRvAdapter = (NoRvAdapter) targetView.getAdapter();
                for (Object data : value) {
                    noRvAdapter.putType(data, annotation.itemType());
                }
                noRvAdapter.setDataList(value);
            }
        }
    }
}
