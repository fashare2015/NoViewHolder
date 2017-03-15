package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.NoViewHolder;
import com.fashare.no_view_holder.R;
import com.fashare.no_view_holder.widget.rv.wrapper.NoHeaderAndFooterWrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 16-11-19.
 *
 * Binder for Header of RecyclerView.
 *
 * The feild annotated by this annotation will be automatically added into RecyclerView's Header
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindRvHeader {
    @IdRes int id();

    @LayoutRes int layout();

    int itemType();

    class Behavior extends IBehavior.Simple<BindRvHeader, RecyclerView, Object>{
        public Behavior() {
            super(BindRvHeader.class, Object.class);
        }

        @Override
        protected int getId(BindRvHeader annotation) {
            return annotation.id();
        }

        @Override
        protected void onBind(NoViewHolder noViewHolder, RecyclerView targetView, BindRvHeader annotation, Object value, final Object dataHolder) {
            if(value == null)
                return ;

            RecyclerView.Adapter adapter = targetView.getAdapter();
//            if(adapter != null) {
                // TODO: 应该放到 "初始化" 流程, 而不是放在 "onBind()" 下
                if(adapter instanceof NoHeaderAndFooterWrapper) {
                    NoHeaderAndFooterWrapper headerAdapter = (NoHeaderAndFooterWrapper) adapter;
                    View itemView = LayoutInflater.from(targetView.getContext()).inflate(annotation.layout(), targetView, false);
                    itemView.setTag(R.id.tag_data_holder, dataHolder);
                    itemView.setTag(R.id.tag_item_type, annotation.itemType());
                    headerAdapter.addHeaderView(itemView);
                    headerAdapter.notifyDataSetChanged();
                }
//            }
        }
    }
}
