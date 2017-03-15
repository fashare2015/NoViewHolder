package com.fashare.no_view_holder.widget;

import android.view.View;

/**
 * Created by jinliangshan on 17/3/9.
 *
 * 公用的点击事件 callback
 */
public interface NoOnItemClickListener<T> {
    void onItemClick(View itemView, T data, int position);
}
