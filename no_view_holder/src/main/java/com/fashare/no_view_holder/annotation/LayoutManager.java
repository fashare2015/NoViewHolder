package com.fashare.no_view_holder.annotation;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-03-09
 * Time: 22:51
 * <br/><br/>
 */
@Retention(RUNTIME)
public @interface LayoutManager {
    Style style() default Style.LINEAR;

    int spanCount() default 1;

    enum Style {
        LINEAR {
            @Override
            RecyclerView.LayoutManager get(Context context, int spanCount) {
                return new LinearLayoutManager(context);
            }
        },
        GRID {
            @Override
            RecyclerView.LayoutManager get(Context context, int spanCount) {
                return new GridLayoutManager(context, spanCount);
            }
        },

        STAGGERED_GRID {
            @Override
            RecyclerView.LayoutManager get(Context context, int spanCount) {
                return new StaggeredGridLayoutManager(spanCount, OrientationHelper.VERTICAL);
            }
        };

        abstract RecyclerView.LayoutManager get(Context context, int spanCount);
    }
}