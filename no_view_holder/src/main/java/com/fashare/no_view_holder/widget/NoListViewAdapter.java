package com.fashare.no_view_holder.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.fashare.no_view_holder.NoViewHolder;

import java.util.ArrayList;
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

    public NoListViewAdapter(Context context, int resource) {
        super(context, resource);
        mLayoutRes = resource;
        mDataList = new ArrayList<>();
    }

    public NoListViewAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        mLayoutRes = resource;
        mDataList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayoutRes, parent, false);
            viewHolder = NoViewHolder.Factory.create(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NoViewHolder) convertView.getTag();
        }

        onBindViewHolder(viewHolder, position);
        return convertView;
    }

    protected void onBindViewHolder(final NoViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(holder.itemView, getItem(position), position);
            }
        });

        T data = getItem(position);
        if(data != null) {
            holder.notifyDataSetChanged(data, position);
        }else{
            Log.e(TAG, String.format("mDataList.get(%d) is null", position));
        }
    }
}
