package com.fashare.no_view_holder.annotation.click;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.widget.rv.wrapper.LoadMoreWrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 16-11-19.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindLoadMore {
    @IdRes int id();

    @LayoutRes int layout();

    class Behavior extends IBehavior.Simple<BindLoadMore, RecyclerView, LoadMoreWrapper.OnLoadMoreListener>{
        public Behavior() {
            super(BindLoadMore.class, LoadMoreWrapper.OnLoadMoreListener.class);
        }

        @Override
        protected int getId(BindLoadMore annotation) {
            return annotation.id();
        }

        @Override
        protected void onInitView(RecyclerView targetView, BindLoadMore annotation, LoadMoreWrapper.OnLoadMoreListener value) {
            if(value == null)
                return ;

            RecyclerView.Adapter adapter = targetView.getAdapter();
            if(adapter instanceof LoadMoreWrapper) {
                LoadMoreWrapper loadMoreWrapper = (LoadMoreWrapper) adapter;
                View itemView = LayoutInflater.from(targetView.getContext()).inflate(annotation.layout(), targetView, false);
                loadMoreWrapper.setLoadMoreView(itemView);
                loadMoreWrapper.setOnLoadMoreListener(value);
            }
        }
    }
}
