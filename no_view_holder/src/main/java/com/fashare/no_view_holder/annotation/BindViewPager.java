package com.fashare.no_view_holder.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewPager;
import android.view.View;

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

    class Behavior extends IBehavior.Simple<BindViewPager, List>{
        public Behavior() {
            super(BindViewPager.class, List.class);
        }

        @Override
        public void onBind(View itemView, final BindViewPager annotation, final List value) {
            final ViewPager vp = (ViewPager) itemView.findViewById(annotation.id());
            bindIfNotNull(vp, annotation.id(), new Runnable() {
                @Override
                public void run() {
                    vp.setAdapter(new NoViewPagerAdapter<>(vp.getContext(), annotation.layout(), value));
                }
            });
        }
    }
}
