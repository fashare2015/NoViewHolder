package com.fashare.no_view_holder.widget.rv;

import android.support.annotation.LayoutRes;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-03-12
 * Time: 20:05
 * <br/><br/>
 */
public abstract class ItemTypeDelegate implements ItemViewDelegate<Object> {
    @LayoutRes int mLayoutRes;
    int mTargetType;

    protected NoRvAdapter mAdapter;

    public ItemTypeDelegate(int layoutRes, int targetType, NoRvAdapter adapter) {
        mLayoutRes = layoutRes;
        mTargetType = targetType;
        mAdapter = adapter;
    }

    @Override
    public int getItemViewLayoutId() {
        return mLayoutRes;
    }

    @Override
    public boolean isForViewType(Object item, int position) {
        return true;
    }

    @Override
    public abstract void convert(ViewHolder holder, Object data, int position);
}
