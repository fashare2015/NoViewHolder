package com.fashare.no_view_holder.widget.rv.wrapper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fashare.no_view_holder.NoViewHolder;
import com.fashare.no_view_holder.R;
import com.fashare.no_view_holder.widget.rv.NoRvAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.utils.WrapperUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by zhy on 16/6/23.
 *
 * copy from base-adapter, not by me!!
 */
public class HeaderAndFooterWrapper extends NoRvAdapter
{
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private Map<Integer, View> mHeaderViews = new TreeMap<>();  // 按 key 排序
    private Map<Integer, View> mFootViews = new TreeMap<>();  // 按 key 排序

    private NoRvAdapter mInnerAdapter;

    public HashMap<ViewHolder, NoViewHolder> getViewHolderConvertor() {
        return mInnerAdapter.getViewHolderConvertor();
    }

    public NoViewHolder getNoViewHolder(ViewHolder holder) {
        return mInnerAdapter.getNoViewHolder(holder);
    }

    public void setClickHolder(Object clickHolder) {
        mInnerAdapter.setClickHolder(clickHolder);
    }

    public Object getClickHolder() {
        return mInnerAdapter.getClickHolder();
    }

    public void setOnItemClickListener(com.fashare.no_view_holder.widget.OnItemClickListener onItemClickListener) {
        mInnerAdapter.setOnItemClickListener(onItemClickListener);
    }

    public void setDataList(List dataList) {
        mInnerAdapter.setDataList(dataList);
    }

    public HeaderAndFooterWrapper(NoRvAdapter adapter) {
        super(null, 0);
        mInnerAdapter = adapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (mHeaderViews.get(viewType) != null)
        {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            NoViewHolder noViewHolder = new NoViewHolder.Factory(mHeaderViews.get(viewType), mInnerAdapter.getClickHolder()).build();
            getViewHolderConvertor().put(holder, noViewHolder);
            return holder;

        } else if (mFootViews.get(viewType) != null)
        {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
            NoViewHolder noViewHolder = new NoViewHolder.Factory(mHeaderViews.get(viewType), mInnerAdapter.getClickHolder())
                    .initView(holder.getConvertView().getTag(R.id.tag_data_holder))
                    .build();
            getViewHolderConvertor().put(holder, noViewHolder);
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position)
    {
        if (isHeaderViewPos(position))
        {
            return (int)new ArrayList(mHeaderViews.keySet()).get(position);
        } else if (isFooterViewPos(position))
        {
            return (int)new ArrayList(mFootViews.keySet()).get(position);
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount()
    {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        if (isHeaderViewPos(position))
        {
            getNoViewHolder(holder).notifyDataSetChanged(holder.getConvertView().getTag(R.id.tag_data_holder));
            return;
        }
        if (isFooterViewPos(position))
        {
            getNoViewHolder(holder).notifyDataSetChanged(holder.getConvertView().getTag(R.id.tag_data_holder));
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount()
    {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback()
        {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position)
            {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null)
                {
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null)
                {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
    {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position))
        {
            WrapperUtils.setFullSpan(holder);
        }
    }

    private boolean isHeaderViewPos(int position)
    {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position)
    {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view)
    {
        mHeaderViews.put((int)view.getTag(R.id.tag_item_type) + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view)
    {
        mFootViews.put((int)view.getTag(R.id.tag_item_type) + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getHeadersCount()
    {
        return mHeaderViews.size();
    }

    public int getFootersCount()
    {
        return mFootViews.size();
    }


}
