package com.fashare.no_view_holder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fashare.no_view_holder.annotation.BindImageView;
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

import butterknife.ButterKnife;

/**
 * Created by jinliangshan on 17/3/9.
 */
public abstract class NoViewHolder<T> extends RecyclerView.ViewHolder {
    protected final String TAG = this.getClass().getSimpleName();

    private static final List<? extends IBehavior<? extends Annotation>> sDataBehaviors = Arrays.asList(
            new BindTextView.Behavior(),
            new BindImageView.Behavior(),
            new BindRecyclerView.Behavior(),
            new BindViewPager.Behavior(),
            new BindListView.Behavior()
    );

    private static final List<? extends IBehavior<? extends Annotation>> sClickBehaviors = Arrays.asList(
            new BindItemClick.Behavior(),
            new BindClick.Behavior()
    );

    private NoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void notifyDataSetChanged(T dataHolder){
        notifyDataSetChanged(dataHolder, 0);
    }

    public void notifyDataSetChanged(T dataHolder, int pos){
        onBind(dataHolder, pos);
    }

    protected abstract void onBind(T dataHolder, int pos);

    public static class Factory{
        public static <T> NoViewHolder<T> create(Activity activity){
            return create(((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0));
        }

        public static <T> NoViewHolder<T> create(final Context context, @LayoutRes int layoutRes, ViewGroup parent){
            return create(LayoutInflater.from(context).inflate(layoutRes, parent, false));
        }

        public static <T> NoViewHolder<T> create(View itemView){
            return new NoViewHolder<T>(itemView) {
                @Override
                protected void onBind(T dataHolder, int pos) {
                    for (Field field : dataHolder.getClass().getDeclaredFields()) {
                        for (IBehavior<? extends Annotation> behavior : NoViewHolder.sDataBehaviors) {
                            if(behavior.isApplyedOn(field))
                                behavior.onBind(itemView, field, dataHolder);
                        }
                    }

                    for (Field field : dataHolder.getClass().getDeclaredFields()) {
                        for (IBehavior<? extends Annotation> behavior : NoViewHolder.sClickBehaviors) {
                            if(behavior.isApplyedOn(field))
                                behavior.onBind(itemView, field, dataHolder);
                        }
                    }
                }
            };
        }
    }
}
