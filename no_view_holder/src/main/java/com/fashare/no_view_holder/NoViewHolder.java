package com.fashare.no_view_holder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.fashare.no_view_holder.annotation.click.BindLoadMore;

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
public final class NoViewHolder extends RecyclerView.ViewHolder {
    protected final String TAG = this.getClass().getSimpleName();

    private final Object mClickHolder;
    private final List<?> mDataHolderList;
    private final Set<Class<?>> mDataHolderClassSet;

    public Object getClickHolder() {
        return mClickHolder;
    }

    // TODO: static
    private static Options mClickOptions = new ClickOptions(),
            mDataOptions = new DataOptions(),
            mExtraOptions = new Options.Default(){
                @Override
                protected Set<? extends IBehavior<? extends Annotation>> getDefaultBehaviors() {
                    return new HashSet<>(Arrays.asList(new BindRvHeader.Behavior()));
                }
            };

    public static void setClickOptions(Options clickOptions) {
        mClickOptions = clickOptions;
    }

    public static void setDataOptions(Options dataOptions) {
        mDataOptions = dataOptions;
    }

    private NoViewHolder(Factory builder) {
        super(builder.mItemView);
        mClickHolder = builder.mClickHolder;
        mDataHolderList = builder.mDataHolderList;
        mDataHolderClassSet = builder.mDataHolderClassSet;

        // 1. 先初始化 view, 以及相关 adapter
        for (Object dataHolder: mDataHolderList) {
            onInitView(dataHolder, mDataOptions);
        }

        // 2. adapter 准备好以后, 嵌套的 @BindRvHeader 额外处理
//        for (Object dataHolder: mDataHolderList) {
//            onInitView(dataHolder, mExtraOptions);
//        }

        // 3. view 和 adapter 都准备好以后, 绑定 click() 和 itemClick()
        onInitView(mClickHolder, mClickOptions);
    }

    private void onInitView(Object holder, Options options) {
        if(holder == null){
            Log.e(TAG, "clickHolder or dataHolder is null!!! Nothing happens.");
            return ;
        }
        for (Field field : holder.getClass().getDeclaredFields()) {
            for (IBehavior<? extends Annotation> behavior : options.getMergedBehaviors()) {
                if(behavior.isAppliedOn(field))
                    behavior.onInitView(this, field, holder);
            }
        }
    }

    public void notifyDataSetChanged(Object dataHolder){
        notifyDataSetChanged(dataHolder, 0);
    }

    public void notifyDataSetChanged(Object dataHolder, int pos){
        if(!mDataHolderClassSet.contains(dataHolder.getClass())){
            // TODO: 是否严格划分 onInitView() 和 onBind(), 还是允许 onBind() 调用 onInitView() 进行 lazy init.
//            throw new IllegalStateException(String.format("You must call NoViewHolder.init(dataHolder) before call NoViewHolder.notifyDataSetChanged(dataHolder)!!! [dataHolder: %s]", dataHolder));
        }
        onBind(dataHolder, pos, mDataOptions);
        onInitView(mClickHolder, mClickOptions);
    }

    private void onBind(Object holder, int pos, Options options) {
        if(holder == null){
            Log.e(TAG, "clickHolder or dataHolder is null!!! Nothing happens.");
            return ;
        }

        for (IBehavior<? extends Annotation> behavior : options.getMergedBehaviors()){
            for (Field field : holder.getClass().getDeclaredFields()) {
                if(behavior.isAppliedOn(field))
                    behavior.onBind(this, field, holder);
            }
        }
    }

    public static class Factory{
        private View mItemView;
        private Object mClickHolder;
        private List<?> mDataHolderList = new ArrayList<>();
        private Set<Class<?>> mDataHolderClassSet = new HashSet<>();

        public Factory(Activity activity){
            this(((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0), activity);
        }

        public Factory(View itemView, Object clickHolder) {
            mItemView = itemView;
            mClickHolder = clickHolder;
        }

        public Factory initView(Object... dataHolderList){
            mDataHolderList = Arrays.asList(dataHolderList);
            for (Object item : dataHolderList)
                mDataHolderClassSet.add(item.getClass());
            return this;
        }

        public NoViewHolder build(){
            return new NoViewHolder(this);
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
                new BindClick.Behavior(),

                new BindLoadMore.Behavior()
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
