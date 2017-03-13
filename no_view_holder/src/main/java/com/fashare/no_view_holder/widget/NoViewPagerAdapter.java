package com.fashare.no_view_holder.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fashare.no_view_holder.NoViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的 PagerAdapter
 * @param <T>
 */
public class NoViewPagerAdapter<T> extends PagerAdapter {

    protected final String TAG = this.getClass().getSimpleName();
    protected Context mContext;
    @LayoutRes int mLayoutRes;
    private List<T> mDataList;
    private OnItemClickListener<T> mOnItemClickListener;

    private Object clickHolder = this;

    /**
     * reutrn POSITION_NONE 使得 notifyDataSetChanged() 会触发 instantiateItem() -> onBindViewHolder()
     * @param object
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(List<T> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setClickHolder(Object clickHolder) {
        this.clickHolder = clickHolder;
    }

    public NoViewPagerAdapter(Context context, @LayoutRes int layoutRes) {
        mContext = context;
        mLayoutRes = layoutRes;
        mDataList = new ArrayList<>();
    }

    public NoViewPagerAdapter(Context context, int layoutRes, List<T> dataList) {
        mContext = context;
        mLayoutRes = layoutRes;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList == null? 0: mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        NoViewHolder viewHolder = createViewHolder(container, position);
        onBindViewHolder(viewHolder, position);
        return viewHolder.itemView;
    }

    protected final NoViewHolder createViewHolder(ViewGroup container, int position) {
        NoViewHolder viewHolder;
        View itemView = LayoutInflater.from(mContext).inflate(mLayoutRes, container, false);
        viewHolder = new NoViewHolder.Factory(itemView, clickHolder)
                .initView(getDataList().get(position))
                .build();

        if(viewHolder != null) {
            container.addView(viewHolder.itemView);
        }else {
            Log.e(TAG, "viewHolder is null");
        }

        return viewHolder;
    }

    public void onBindViewHolder(final NoViewHolder holder, final int position) {
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
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

}
