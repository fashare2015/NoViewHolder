package com.fashare.no_view_holder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fashare.no_view_holder.annotation.BindImageView;
import com.fashare.no_view_holder.annotation.BindImageViews;
import com.fashare.no_view_holder.annotation.BindListView;
import com.fashare.no_view_holder.annotation.BindRecyclerView;
import com.fashare.no_view_holder.annotation.BindTextView;
import com.fashare.no_view_holder.annotation.BindViewPager;
import com.fashare.no_view_holder.annotation.click.BindClick;
import com.fashare.no_view_holder.annotation.click.BindItemClick;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jinliangshan on 17/3/9.
 */
public final class NoViewHolder<T> extends RecyclerView.ViewHolder {
    protected final String TAG = this.getClass().getSimpleName();

    private static final List<? extends IBehavior<? extends Annotation>> sDataBehaviors = Arrays.asList(
            new BindTextView.Behavior(),
            new BindImageView.Behavior(),

            new BindRecyclerView.Behavior(),
            new BindViewPager.Behavior(),
            new BindListView.Behavior(),

            new BindImageViews.Behavior()
    );

    private static final List<? extends IBehavior<? extends Annotation>> sClickBehaviors = Arrays.asList(
            new BindItemClick.Behavior(),
            new BindClick.Behavior()
    );

    private final Object mClickHolder;

    private NoViewHolder(View itemView, Object clickHolder) {
        super(itemView);
        mClickHolder = clickHolder;
        onBindClick(clickHolder);
    }

    public void notifyDataSetChanged(T dataHolder){
        notifyDataSetChanged(dataHolder, 0);
    }

    public void notifyDataSetChanged(T dataHolder, int pos){
        onBind(dataHolder, pos);
        onBindClick(mClickHolder);
    }

    private void onBind(T dataHolder, int pos) {
        for (Field field : dataHolder.getClass().getDeclaredFields()) {
            for (IBehavior<? extends Annotation> behavior : NoViewHolder.sDataBehaviors) {
                if(behavior.isApplyedOn(field))
                    behavior.onBind(itemView, field, dataHolder);
            }
        }
    }

    private void onBindClick(Object clickHolder){
        for (Field field : clickHolder.getClass().getDeclaredFields()) {
            for (IBehavior<? extends Annotation> behavior : NoViewHolder.sClickBehaviors) {
                if(behavior.isApplyedOn(field))
                    behavior.onBind(itemView, field, clickHolder);
            }
        }
    }

    public static class Factory{
        public static <T> NoViewHolder<T> create(Activity activity){
            return create(((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0), activity);
        }

        public static <T> NoViewHolder<T> create(View itemView){
            return create(itemView, itemView);
        }

        public static <T> NoViewHolder<T> create(View itemView, Object clickHolder){
            return new NoViewHolder<>(itemView, clickHolder);
        }
    }
}
