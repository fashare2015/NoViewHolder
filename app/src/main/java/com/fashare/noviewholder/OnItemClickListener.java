package com.fashare.noviewholder;

import android.view.View;

/**
 * Created by jinliangshan on 17/3/9.
 */
public interface OnItemClickListener<T> {
    void onItemClick(View itemView, T data, int position);
}
