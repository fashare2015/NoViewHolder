package com.fashare.no_view_holder.widget.rv;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.fashare.no_view_holder.NoViewHolder;
import com.fashare.no_view_holder.widget.NoOnItemClickListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-03-12
 * Time: 17:20
 *
 * 基于 {@link CommonAdapter} 修改.
 *
 * 1. 封装了 {@link NoViewHolder}
 *
 * 2. 仅支持单类型
 */
public class NoRvAdapter<T> extends MultiItemTypeAdapter<T> {
    protected final String TAG = this.getClass().getSimpleName();
    @LayoutRes int mLayoutRes;
    private NoOnItemClickListener<T> mNoOnItemClickListener;

    HashMap<ViewHolder, NoViewHolder> mViewHolderConvertor = new HashMap<>();

    // 加 header 嵌套布局时, 要传入 clickHolder, 以便把点击事件串起来。
    private Object clickHolder = this;

    public HashMap<ViewHolder, NoViewHolder> getViewHolderConvertor() {
        return mViewHolderConvertor;
    }

    public NoViewHolder getNoViewHolder(ViewHolder holder) {
        return mViewHolderConvertor.get(holder);
    }

    public void setClickHolder(Object clickHolder) {
        this.clickHolder = clickHolder;
    }

    public Object getClickHolder() {
        return clickHolder;
    }

    public void setNoOnItemClickListener(NoOnItemClickListener<T> noOnItemClickListener) {
        mNoOnItemClickListener = noOnItemClickListener;
    }

    public void setDataList(List<T> dataList) {
        mDatas = dataList;
    }

    public NoRvAdapter(Context context, @LayoutRes int layoutRes) {
        this(context, layoutRes, new ArrayList<T>());
    }

    public NoRvAdapter(Context context, int layoutRes, List<T> dataList) {
        super(context, dataList);
        mLayoutRes = layoutRes;

        // 单类型 Delegate
        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return mLayoutRes;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;    // return true: 不区分 itemType
            }

            @Override
            public void convert(ViewHolder holder, T data, int position) {
                getNoViewHolder(holder).notifyDataSetChanged(data, position);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        // 加 header 嵌套布局时, 要传入 clickHolder, 以便把点击事件串起来(传给header)。
        int pos = holder.getAdapterPosition();
        NoViewHolder noViewHolder = new NoViewHolder.Builder(holder.itemView, clickHolder)
                .initView(getDatas().get(0))    // pos 任意皆可, 用于初始化 data 中的注解
                .build();
        mViewHolderConvertor.put(holder, noViewHolder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNoOnItemClickListener != null) {
                    mNoOnItemClickListener.onItemClick(holder.itemView, getDatas().get(position), position);
                }
            }
        });
    }
}
