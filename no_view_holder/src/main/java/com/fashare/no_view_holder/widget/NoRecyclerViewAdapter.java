package com.fashare.no_view_holder.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fashare.no_view_holder.NoViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinliangshan on 16/8/25.
 */
public class NoRecyclerViewAdapter<T> extends RecyclerView.Adapter<NoViewHolder<T>>{
    protected final String TAG = this.getClass().getSimpleName();
    protected Context mContext;
    @LayoutRes int mLayoutRes;
    private List<T> mDataList;
    private OnItemClickListener<T> mOnItemClickListener;

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(List<T> dataList) {
        mDataList = dataList;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public NoRecyclerViewAdapter(Context context, @LayoutRes int layoutRes) {
        mContext = context;
        mLayoutRes = layoutRes;
        mDataList = new ArrayList<>();
    }

    public NoRecyclerViewAdapter(Context context, int layoutRes, List<T> dataList) {
        mContext = context;
        mLayoutRes = layoutRes;
        mDataList = dataList;
    }

    @Override
    public NoViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return NoViewHolder.Factory.create(LayoutInflater.from(mContext).inflate(mLayoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(final NoViewHolder<T> holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(holder.itemView, mDataList.get(position), position);
            }
        });

        T data = getDataList().get(position);
        if(data != null) {
            holder.notifyDataSetChanged(data, position);
        }else{
            Log.e(TAG, String.format("mDataList.get(%d) is null", position));
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null? 0: mDataList.size();
    }

}
