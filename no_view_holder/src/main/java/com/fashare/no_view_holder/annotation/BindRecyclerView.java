package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.NoViewHolder;
import com.fashare.no_view_holder.widget.rv.ItemTypeDelegate;
import com.fashare.no_view_holder.widget.rv.NoRvAdapter;
import com.fashare.no_view_holder.widget.rv.wrapper.HeaderAndFooterWrapper;
import com.fashare.no_view_holder.widget.rv.wrapper.LoadMoreWrapper;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
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
        protected void onInitView(NoViewHolder noViewHolder, RecyclerView targetView, BindRecyclerView annotation, List value) {
            if(value == null)
                value = new ArrayList();

            LayoutManager lm = annotation.layoutManager();
            targetView.setLayoutManager(lm.style().get(targetView.getContext(), lm.spanCount()));
            final NoRvAdapter noRvAdapter = new NoRvAdapter(targetView.getContext(), annotation.layout(), value);
            noRvAdapter.addItemViewDelegate(new ItemTypeDelegate(annotation.layout(), annotation.itemType(), noRvAdapter){
                @Override
                public void convert(ViewHolder holder, Object data, int position) {
                    noRvAdapter.getNoViewHolder(holder).notifyDataSetChanged(data, position);
                }
            });
            // 加 header 嵌套布局时, 要传入 clickHolder, 以便把点击事件串起来。
            noRvAdapter.setClickHolder(noViewHolder.getClickHolder());
            targetView.setAdapter(new LoadMoreWrapper(new HeaderAndFooterWrapper(noRvAdapter)));
        }

        @Override
        protected void onBind(NoViewHolder noViewHolder, RecyclerView targetView, BindRecyclerView annotation, List value) {
            if(value == null)
                value = new ArrayList();

            RecyclerView.Adapter adapter = targetView.getAdapter();
            if(adapter == null) {
                onInitView(noViewHolder, targetView, annotation, value); // TODO:
                adapter = targetView.getAdapter();
            }

            if(adapter instanceof NoRvAdapter) {
                NoRvAdapter noRvAdapter = (NoRvAdapter) targetView.getAdapter();
                noRvAdapter.setDataList(value);
                adapter.notifyDataSetChanged();
            }else
                adapter.notifyDataSetChanged();
        }
    }
}
