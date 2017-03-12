package com.fashare.no_view_holder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fashare.no_view_holder.annotation.BindImageView;
import com.fashare.no_view_holder.annotation.BindImageViews;
import com.fashare.no_view_holder.annotation.BindListView;
import com.fashare.no_view_holder.annotation.BindRecyclerView;
import com.fashare.no_view_holder.annotation.BindRvHeader;
import com.fashare.no_view_holder.annotation.BindTextView;
import com.fashare.no_view_holder.annotation.BindViewPager;
import com.fashare.no_view_holder.annotation.click.BindClick;
import com.fashare.no_view_holder.annotation.click.BindItemClick;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jinliangshan on 17/3/9.
 */
public final class NoViewHolder<T> extends RecyclerView.ViewHolder {
    protected final String TAG = this.getClass().getSimpleName();

    private final Object mClickHolder;

    // TODO: static
    private static Options mClickOptions = new ClickOptions(),
            mDataOptions = new DataOptions();

    public NoViewHolder<T> setClickOptions(Options clickOptions) {
        mClickOptions = clickOptions;
        return this;
    }

    public NoViewHolder<T> setDataOptions(Options dataOptions) {
        mDataOptions = dataOptions;
        return this;
    }

    private NoViewHolder(View itemView, Object clickHolder) {
        super(itemView);
        mClickHolder = clickHolder;
        onBind(clickHolder, 0, mClickOptions);
    }

    public void notifyDataSetChanged(T dataHolder){
        notifyDataSetChanged(dataHolder, 0);
    }

    public void notifyDataSetChanged(T dataHolder, int pos){
        onBind(dataHolder, pos, mDataOptions);
        onBind(mClickHolder, 0, mClickOptions);
    }

    private void onBind(Object holder, int pos, Options options) {
        for (Field field : holder.getClass().getDeclaredFields()) {
            for (IBehavior<? extends Annotation> behavior : options.getMergedBehaviors()) {
                if(behavior.isApplyedOn(field))
                    behavior.onBind(itemView, field, holder);
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

    public interface Options{
        Set<? extends IBehavior<? extends Annotation>> getMergedBehaviors();

        Options setBehaviors(IBehavior<? extends Annotation>... customizedBehaviors);

        abstract class Default implements Options{
            protected List<IBehavior<? extends Annotation>> mCustomizedDataBehaviors = new ArrayList<>();

            @Override
            final public Set<? extends IBehavior<? extends Annotation>> getMergedBehaviors() {
                Set<IBehavior<? extends Annotation>> behaviors = new HashSet<>();

//                Log.d("Default", "mCustomizedDataBehaviors:" + mCustomizedDataBehaviors);
                behaviors.addAll(mCustomizedDataBehaviors); // 先加载 用户自定义的 Behaviors
                behaviors.addAll(getDefaultBehaviors());    // 再加载 默认的, 有重复的不会加进去
                return behaviors;
            }

            protected abstract Set<? extends IBehavior<? extends Annotation>> getDefaultBehaviors();

            @Override
            public NoViewHolder.Options setBehaviors(IBehavior<? extends Annotation>... customizedBehaviors) {
                mCustomizedDataBehaviors.addAll(Arrays.asList(customizedBehaviors));
                return this;
            }
        }
    }

    public static class ClickOptions extends Options.Default{
        private static final List<? extends IBehavior<? extends Annotation>> sClickBehaviors = Arrays.asList(
                new BindItemClick.Behavior(),
                new BindClick.Behavior()
        );

        @Override
        protected Set<? extends IBehavior<? extends Annotation>> getDefaultBehaviors() {
            return new HashSet<>(sClickBehaviors);
        }
    }

    public static class DataOptions extends Options.Default{
        private static final List<? extends IBehavior<? extends Annotation>> sDataBehaviors = Arrays.asList(
                new BindTextView.Behavior(),
                new BindImageView.Behavior(),

                new BindRecyclerView.Behavior(),
                new BindRvHeader.Behavior(),
                new BindViewPager.Behavior(),
                new BindListView.Behavior(),

                new BindImageViews.Behavior()
        );

        @Override
        protected Set<? extends IBehavior<? extends Annotation>> getDefaultBehaviors() {
            return new HashSet<>(sDataBehaviors);
        }
    }
}
