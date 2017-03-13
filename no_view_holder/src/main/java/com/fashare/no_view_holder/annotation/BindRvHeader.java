package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.NoViewHolder;
import com.fashare.no_view_holder.widget.rv.ItemTypeDelegate;
import com.fashare.no_view_holder.widget.rv.NoRvAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 16-11-19.
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
                NoRvAdapter headerAdapter = null;
                if(adapter instanceof NoRvAdapter) {
                    headerAdapter = (NoRvAdapter) adapter;

                    headerAdapter.addItemViewDelegate(new ItemTypeDelegate(annotation.layout(), annotation.itemType(), headerAdapter){
                        @Override
                        public void convert(ViewHolder holder, Object data, int position) {
                            mAdapter.getNoViewHolder(holder).notifyDataSetChanged(dataHolder);  // BindRvHeader 仅处理了setHead(), 需要递归解析 BindXXX
                        }
                    });
                    headerAdapter.putType(value, annotation.itemType());
                    headerAdapter.getDatas().add(0, value);
                    headerAdapter.notifyDataSetChanged();
                }
//            }
        }
    }
}
