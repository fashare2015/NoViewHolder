package com.fashare.no_view_holder.annotation.click;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.widget.rv.wrapper.NoLoadMoreWrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 16-11-19.
 *
 * LoadMore Callback for NoLoadMoreWrapper
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindLoadMore {
    @IdRes int id();

    @LayoutRes int layout();

    class Behavior extends IBehavior.Simple<BindLoadMore, RecyclerView, NoLoadMoreWrapper.OnLoadMoreListener>{
        public Behavior() {
            super(BindLoadMore.class, NoLoadMoreWrapper.OnLoadMoreListener.class);
        }

        @Override
        protected int getId(BindLoadMore annotation) {
            return annotation.id();
        }

        @Override
        protected void onInitView(RecyclerView targetView, BindLoadMore annotation, NoLoadMoreWrapper.OnLoadMoreListener value) {
            if(value == null)
                return ;

            RecyclerView.Adapter adapter = targetView.getAdapter();
            if(adapter instanceof NoLoadMoreWrapper) {
                NoLoadMoreWrapper noLoadMoreWrapper = (NoLoadMoreWrapper) adapter;
                View itemView = LayoutInflater.from(targetView.getContext()).inflate(annotation.layout(), targetView, false);
                noLoadMoreWrapper.setLoadMoreView(itemView);
                noLoadMoreWrapper.setOnLoadMoreListener(value);
            }
        }
    }
}
