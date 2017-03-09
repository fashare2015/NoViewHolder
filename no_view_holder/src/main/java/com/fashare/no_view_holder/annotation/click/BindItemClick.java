package com.fashare.no_view_holder.annotation.click;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.widget.NoListViewAdapter;
import com.fashare.no_view_holder.widget.NoRecyclerViewAdapter;
import com.fashare.no_view_holder.widget.NoViewPagerAdapter;
import com.fashare.no_view_holder.widget.OnItemClickListener;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 17-3-10.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindItemClick {
    @IdRes int id();

    class Behavior extends IBehavior.Simple<BindItemClick, OnItemClickListener>{
        public Behavior() {
            super(BindItemClick.class, OnItemClickListener.class);
        }

        @Override
        public void onBind(View itemView, final BindItemClick annotation, final OnItemClickListener value) {
            final View view = itemView.findViewById(annotation.id());
            bindIfNotNull(view, annotation.id(), new Runnable() {
                @Override
                public void run() {
                    if(view instanceof ListView){
                        ListView lv = (ListView) view;
                        NoListViewAdapter adapter = (NoListViewAdapter) lv.getAdapter();
                        adapter.setOnItemClickListener(value);

                    }else if(view instanceof RecyclerView){
                        RecyclerView rv = (RecyclerView) view;
                        NoRecyclerViewAdapter adapter = (NoRecyclerViewAdapter) rv.getAdapter();
                        adapter.setOnItemClickListener(value);

                    }else if(view instanceof ViewPager){
                        ViewPager vp = (ViewPager) view;
                        NoViewPagerAdapter adapter = (NoViewPagerAdapter) vp.getAdapter();
                        adapter.setOnItemClickListener(value);
                    }
                }
            });
        }
    }
}
