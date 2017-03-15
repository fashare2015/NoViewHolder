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
 *
 * 门面模式, 对外交互唯一接口.
 * UI 容器: 封装了所有 dataHolder 和 clickHolder 中所注解的 View 实例.
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
            mDataOptions = new DataOptions();

    /**
     * 全局配置 ClickOptions: {@link BindClick}, {@link BindItemClick}, {@link BindLoadMore}
     * @param clickOptions
     */
    public static void setClickOptions(Options clickOptions) {
        mClickOptions = clickOptions;
    }

    /**
     * 全局配置 DataOptions: 其他所有 BindXXX
     * @param dataOptions
     */
    public static void setDataOptions(Options dataOptions) {
        mDataOptions = dataOptions;
    }

    private NoViewHolder(Builder builder) {
        super(builder.mItemView);
        mClickHolder = builder.mClickHolder;
        mDataHolderList = builder.mDataHolderList;
        mDataHolderClassSet = builder.mDataHolderClassSet;

        // 1. 先初始化 view, 以及相关 adapter
        for (Object dataHolder: mDataHolderList) {
            onInitView(dataHolder, mDataOptions);
        }

        // 2. view 和 adapter 都准备好以后, 绑定 click() 和 itemClick()
        onInitView(mClickHolder, mClickOptions);
    }

    /**
     * 通知 dataHolder 发生改变, 更新 UI.
     * 通常调用于 Activity, Fragment, 自定义View 等.
     *
     * @param dataHolder
     */
    public void notifyDataSetChanged(Object dataHolder){
        notifyDataSetChanged(dataHolder, 0);
    }

    /**
     * 通知 dataHolder 发生改变, 更新 UI.
     * 通常调用于 各种 Adapter, 需带上一个 position.
     *
     * @param dataHolder
     * @param pos
     */
    public void notifyDataSetChanged(Object dataHolder, int pos){
        if(!mDataHolderClassSet.contains(dataHolder.getClass())){
            // TODO: 是否严格划分 onInitView() 和 onBind(), 还是允许 onBind() 调用 onInitView() 进行 lazy init.
//            throw new IllegalStateException(String.format("You must call NoViewHolder.init(dataHolder) before call NoViewHolder.notifyDataSetChanged(dataHolder)!!! [dataHolder: %s]", dataHolder));
        }
        onBind(dataHolder, pos, mDataOptions);
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

    /**
     * Builder 模式
     */
    public static class Builder {
        private View mItemView;
        private Object mClickHolder;
        private List<?> mDataHolderList = new ArrayList<>();
        private Set<Class<?>> mDataHolderClassSet = new HashSet<>();

        /**
         * 为方便 Activity 使用而加的构造器.
         *
         * 1. ContentView 作为 itemView.
         * 2. activity 作为 clickHolder.
         *
         * @param activity
         */
        public Builder(Activity activity){
            this(((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0), activity);
        }

        /**
         * 1. itemView 作为 rootView, 之后 dataHolder 里注解的 R.id.XXX 要确保在这个 rootView 布局中.
         * 2. 指定一个 clickHolder 存放你的 OnClick 回调, 通常用 this 即可.
         *
         * @param itemView 即 {@link RecyclerView.ViewHolder#itemView}, 相当于 rootView
         * @param clickHolder BindOnClick 等注解所在的类.
         */
        public Builder(View itemView, Object clickHolder) {
            mItemView = itemView;
            mClickHolder = clickHolder;
        }

        /**
         * 必须注入 "带有注解配置的 dataHolder", 以便根据 R.id.XXX 初始化相应的 View 和 Adapter.
         * 为后续的 {@link #notifyDataSetChanged(Object)} 做准备, 否则可能导致找不到控件.
         *
         * @param dataHolderList new 一个带有注解配置的类, 如 new HomeInfo()
         * @return
         */
        public Builder initView(Object... dataHolderList){
            mDataHolderList = Arrays.asList(dataHolderList);
            for (Object item : dataHolderList)
                mDataHolderClassSet.add(item.getClass());
            return this;
        }

        public NoViewHolder build(){
            return new NoViewHolder(this);
        }
    }

    /**
     * BindXXX.Behavior 的集合
     */
    public interface Options{
        /**
         * 合并 "用户自定义Behavior" 和 "内置Behavior".
         *
         * 前者可以覆盖(override)后者
         * @return
         */
        Set<? extends IBehavior<? extends Annotation>> getMergedBehaviors();

        /**
         * 设置 "用户自定义Behavior"
         * @param customizedBehaviors
         * @return
         */
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
