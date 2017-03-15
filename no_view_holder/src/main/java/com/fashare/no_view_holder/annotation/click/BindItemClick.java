package com.fashare.no_view_holder.annotation.click;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.widget.NoListViewAdapter;
import com.fashare.no_view_holder.widget.NoViewPagerAdapter;
import com.fashare.no_view_holder.widget.NoOnItemClickListener;
import com.fashare.no_view_holder.widget.rv.NoRvAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 17-3-10.
 *
 * 点击事件 for ListView、RecyclerView、ViewPager
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindItemClick {
    @IdRes int id();

    class Behavior extends IBehavior.Simple<BindItemClick, ViewGroup, NoOnItemClickListener>{
        public Behavior() {
            super(BindItemClick.class, NoOnItemClickListener.class);
        }

        @Override
        protected int getId(BindItemClick annotation) {
            return annotation.id();
        }

        @Override
        protected void onInitView(ViewGroup itemView, BindItemClick annotation, NoOnItemClickListener value) {
            final View view = itemView.findViewById(annotation.id());
            if(view instanceof ListView){
                ListView lv = (ListView) view;
                NoListViewAdapter adapter = (NoListViewAdapter) lv.getAdapter();
                if(adapter != null)
                    adapter.setNoOnItemClickListener(value);

            }else if(view instanceof RecyclerView){
                RecyclerView rv = (RecyclerView) view;
                NoRvAdapter adapter = (NoRvAdapter) rv.getAdapter();
                if(adapter != null)
                    adapter.setNoOnItemClickListener(value);

            }else if(view instanceof ViewPager){
                ViewPager vp = (ViewPager) view;
                NoViewPagerAdapter adapter = (NoViewPagerAdapter) vp.getAdapter();
                if(adapter != null)
                    adapter.setNoOnItemClickListener(value);

            }else{
                throw new UnsupportedOperationException(String.format("Field[%s] which annotated by %s must instance of %s、%s or %s!!!",
                        itemView, annotation.getClass().getSimpleName(), "ListView", "RecyclerView", "ViewPager")
                );
            }
        }
    }
}
