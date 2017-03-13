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

    HashMap<Object, Integer> mTypeRecorder = new HashMap<>();
    HashMap<ViewHolder, NoViewHolder> mViewHolderConvertor = new HashMap<>();

    public HashMap<Object, Integer> getTypeRecorder() {
        return mTypeRecorder;
    }

    public void putType(Object obj, int itemType){
        mTypeRecorder.put(obj, itemType);
    }

    public int getType(Object item) {
        Integer itemType = mTypeRecorder.get(item);
        return itemType != null? itemType: 0;
    }

    public HashMap<ViewHolder, NoViewHolder> getViewHolderConvertor() {
        return mViewHolderConvertor;
    }

    public NoViewHolder getNoViewHolder(ViewHolder holder) {
        return mViewHolderConvertor.get(holder);
    }

    public void setDataList(List<T> dataList) {
        mDatas = dataList;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(com.fashare.no_view_holder.widget.OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public NoRvAdapter(Context context, @LayoutRes int layoutRes) {
        this(context, layoutRes, new ArrayList<T>());
    }

    public NoRvAdapter(Context context, int layoutRes, List<T> dataList) {
        super(context, dataList);
        mLayoutRes = layoutRes;
    }

    @Override
    public void onViewHolderCreated(ViewHolder holder, View itemView) {
//        Log.d(TAG, "onViewHolderCreated: ");
        NoViewHolder noViewHolder = NoViewHolder.Factory.create(holder.itemView);
        mViewHolderConvertor.put(holder, noViewHolder);
    }

    @Override
    protected void setListener(ViewGroup parent, final ViewHolder holder, int viewType) {
        super.setListener(parent, holder, viewType);
//        Log.d(TAG, "setListener: ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, getDatas().get(position), position);
                }
            }
        });
    }
}
