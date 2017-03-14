package com.fashare.no_view_holder.widget.rv;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.fashare.no_view_holder.NoViewHolder;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-03-12
 * Time: 17:20
 * <br/><br/>
 */
public class NoRvAdapter<T> extends MultiItemTypeAdapter<T> {
    protected final String TAG = this.getClass().getSimpleName();
    @LayoutRes int mLayoutRes;
    private com.fashare.no_view_holder.widget.OnItemClickListener<T> mOnItemClickListener;

    HashMap<ViewHolder, NoViewHolder> mViewHolderConvertor = new HashMap<>();

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

    public void setOnItemClickListener(com.fashare.no_view_holder.widget.OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setDataList(List<T> dataList) {
        mDatas = dataList;
        notifyDataSetChanged();
    }

    public NoRvAdapter(Context context, @LayoutRes int layoutRes) {
        this(context, layoutRes, new ArrayList<T>());
    }

    public NoRvAdapter(Context context, int layoutRes, List<T> dataList) {
        super(context, dataList);
        mLayoutRes = layoutRes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        // 加 header 嵌套布局时, 要传入 clickHolder, 以便把点击事件串起来(传给header)。
        int pos = holder.getAdapterPosition();
        NoViewHolder noViewHolder = new NoViewHolder.Factory(holder.itemView, clickHolder)
//                .initView(getDatas().get(0))    // TODO: pos ???
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
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.itemView, getDatas().get(position), position);
                }
            }
        });
    }
}
