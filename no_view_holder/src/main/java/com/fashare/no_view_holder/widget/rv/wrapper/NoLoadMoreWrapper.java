package com.fashare.no_view_holder.widget.rv.wrapper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.fashare.no_view_holder.NoViewHolder;
import com.fashare.no_view_holder.widget.NoOnItemClickListener;
import com.fashare.no_view_holder.widget.rv.NoRvAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.utils.WrapperUtils;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.HashMap;
import java.util.List;


/**
 * Created by zhy on 16/6/23.
 *
 * 基于 {@link LoadMoreWrapper} 修改.
 *
 * 1. 封装了 {@link NoViewHolder}
 */
public class NoLoadMoreWrapper extends NoHeaderAndFooterWrapper
{
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private NoRvAdapter mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId;

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

    public void setNoOnItemClickListener(NoOnItemClickListener noOnItemClickListener) {
        mInnerAdapter.setNoOnItemClickListener(noOnItemClickListener);
    }

    public void setDataList(List dataList) {
        mInnerAdapter.setDataList(dataList);
    }

    public NoLoadMoreWrapper(NoRvAdapter adapter)
    {
        super(adapter);
        mInnerAdapter = adapter;
    }

    private boolean hasLoadMore()
    {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0;
    }


    private boolean isShowLoadMore(int position)
    {
        return hasLoadMore() && (position >= mInnerAdapter.getItemCount());
    }

    @Override
    public int getItemViewType(int position)
    {
        if (isShowLoadMore(position))
        {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == ITEM_TYPE_LOAD_MORE)
        {
            ViewHolder holder;
            if (mLoadMoreView != null)
            {
                holder = ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
                // 不需要继续绑定
//                NoViewHolder noViewHolder = new NoViewHolder.Factory(holder.getConvertView(), mInnerAdapter.getClickHolder()).build();
//                getViewHolderConvertor().put(holder, noViewHolder);
            } else
            {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mLoadMoreLayoutId);
//                NoViewHolder noViewHolder = new NoViewHolder.Factory(holder.getConvertView(), mInnerAdapter.getClickHolder()).build();
//                getViewHolderConvertor().put(holder, noViewHolder);
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        if (isShowLoadMore(position))
        {
            if (mOnLoadMoreListener != null)
            {
                mOnLoadMoreListener.onLoadMoreRequested();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    //TODO: 是这样代理么 ???
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback()
        {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position)
            {
                if (isShowLoadMore(position))
                {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
    {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (isShowLoadMore(holder.getLayoutPosition()))
        {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder)
    {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams)
        {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount()
    {
        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }


    public interface OnLoadMoreListener
    {
        void onLoadMoreRequested();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public NoLoadMoreWrapper setOnLoadMoreListener(OnLoadMoreListener loadMoreListener)
    {
        if (loadMoreListener != null)
        {
            mOnLoadMoreListener = loadMoreListener;
        }
        return this;
    }

    public NoLoadMoreWrapper setLoadMoreView(View loadMoreView)
    {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public NoLoadMoreWrapper setLoadMoreView(int layoutId)
    {
        mLoadMoreLayoutId = layoutId;
        return this;
    }

    // 代理 NoHeaderAndFooterWrapper
    public void addHeaderView(View view)
    {
        if(mInnerAdapter instanceof NoHeaderAndFooterWrapper)
            ((NoHeaderAndFooterWrapper) mInnerAdapter).addHeaderView(view);
    }

    public void addFootView(View view)
    {
        if(mInnerAdapter instanceof NoHeaderAndFooterWrapper)
            ((NoHeaderAndFooterWrapper) mInnerAdapter).addFootView(view);
    }

    public int getHeadersCount()
    {
        if(mInnerAdapter instanceof NoHeaderAndFooterWrapper)
            ((NoHeaderAndFooterWrapper) mInnerAdapter).getHeadersCount();
        return 0;
    }

    public int getFootersCount()
    {
        if(mInnerAdapter instanceof NoHeaderAndFooterWrapper)
            ((NoHeaderAndFooterWrapper) mInnerAdapter).getHeadersCount();
        return 0;
    }
}
