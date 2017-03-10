package com.fashare.no_view_holder.annotation.click;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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

    class Behavior extends IBehavior.Simple<BindItemClick, ViewGroup, OnItemClickListener>{
        public Behavior() {
            super(BindItemClick.class, OnItemClickListener.class);
        }

        @Override
        protected int getId(BindItemClick annotation) {
            return annotation.id();
        }

        @Override
        public void onBind(ViewGroup itemView, BindItemClick annotation, OnItemClickListener value) {
            final View view = itemView.findViewById(annotation.id());
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

                    }else{
                        throw new UnsupportedOperationException(String.format("Field[%s] which annotated by %s must instance of %s、%s or %s!!!",
                                itemView, annotation.getClass().getSimpleName(), "ListView", "RecyclerView", "ViewPager")
                        );
                    }
        }
    }
}
