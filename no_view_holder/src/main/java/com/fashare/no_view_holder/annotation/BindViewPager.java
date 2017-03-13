package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.fashare.no_view_holder.IBehavior;
import com.fashare.no_view_holder.widget.NoViewPagerAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by apple on 16-11-19.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindViewPager {
    @IdRes int id();

    @LayoutRes int layout();

    class Behavior extends IBehavior.Simple<BindViewPager, ViewPager, List>{
        public Behavior() {
            super(BindViewPager.class, List.class);
        }

        @Override
        protected int getId(BindViewPager annotation) {
            return annotation.id();
        }

        @Override
        protected void onBind(ViewPager targetView, BindViewPager annotation, List value) {
            PagerAdapter adapter = targetView.getAdapter();
            if(adapter == null)
                targetView.setAdapter(new NoViewPagerAdapter<>(targetView.getContext(), annotation.layout(), value));
            else if(adapter instanceof NoViewPagerAdapter)
                ((NoViewPagerAdapter) targetView.getAdapter()).setDataList(value);
        }
    }
}
