package com.fashare.no_view_holder.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.fashare.no_view_holder.NoViewHolder;

import java.util.List;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-03-09
 * Time: 23:32
 * <br/><br/>
 */
public class NoListViewAdapter<T> extends ArrayAdapter<T> {
    protected final String TAG = this.getClass().getSimpleName();
    @LayoutRes int mLayoutRes;
    private OnItemClickListener<T> mOnItemClickListener;

    public OnItemClickListener<T> getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public NoListViewAdapter(Context context, int resource) {
        super(context, resource);
        mLayoutRes = resource;
    }

    public NoListViewAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        mLayoutRes = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoViewHolder<T> viewHolder;
        if (convertView == null) {
            viewHolder = NoViewHolder.Factory.create(getContext(), mLayoutRes, parent);
            convertView = viewHolder.itemView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NoViewHolder<T>) convertView.getTag();
        }

        onBindViewHolder(viewHolder, position);
        return convertView;
    }

    protected void onBindViewHolder(final NoViewHolder<T> holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(holder.itemView, getItem(position), position);
            }
        });

        T data = getItem(position);
        if(data != null) {
            holder.bind(data, position);
        }else{
            Log.e(TAG, String.format("mDataList.get(%d) is null", position));
        }
    }
}