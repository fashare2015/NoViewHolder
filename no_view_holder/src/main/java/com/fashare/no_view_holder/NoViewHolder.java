package com.fashare.no_view_holder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fashare.no_view_holder.annotation.BindImageView;
import com.fashare.no_view_holder.annotation.BindTextView;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * Created by jinliangshan on 17/3/9.
 */
public abstract class NoViewHolder<T> extends RecyclerView.ViewHolder {
    protected final String TAG = this.getClass().getSimpleName();

    public NoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void onBind(T data, int pos);

    public abstract void onRecycled();

    public static class Factory{
        public static <T> NoViewHolder<T> create(final Context context, @LayoutRes int layoutRes, ViewGroup parent){
            return new NoViewHolder<T>(LayoutInflater.from(context).inflate(layoutRes, parent, false)) {
                protected final String TAG = "NoViewHolder.Factory";

                @Override
                public void onBind(T data, int pos) {
                    Class<T> dataClazz = (Class<T>)data.getClass();
                    Log.d(TAG, "dataClazz:" + dataClazz);
                    Field[] fields = dataClazz.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        try {
                            Log.d(TAG, "name:" + field.getName() + ", value:" + field.get(data));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        if(field.getAnnotation(BindTextView.class) != null){
                            Log.d(TAG, String.format("%s.%s which annotated be BindTextView must be String!!!", dataClazz.getSimpleName(), field.getName()));
                            BindTextView bindTextView = field.getAnnotation(BindTextView.class);
                            try {
                                field.setAccessible(true);
                                Object text = field.get(data);
                                if(text instanceof String) {
                                    ((TextView) itemView.findViewById(bindTextView.id())).setText((String) text);
                                }else
                                    throw new IllegalStateException(String.format("%s.%s which annotated be BindTextView must be String!!!", dataClazz.getSimpleName(), field.getName()));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }


                        }else if(field.getAnnotation(BindImageView.class) != null){
                            Log.d(TAG, String.format("%s.%s which annotated be BindImageView must be String!!!", dataClazz.getSimpleName(), field.getName()));
                            BindImageView bindImageView = field.getAnnotation(BindImageView.class);
                            try {
                                field.setAccessible(true);
                                Object imgUrl = field.get(data);
                                if(imgUrl instanceof String) {
                                    ImageLoader.loadImage((ImageView) itemView.findViewById(bindImageView.id()),
                                            (String)imgUrl,
                                            bindImageView.placeHolder()
                                    );

                                }else
                                    throw new IllegalStateException(String.format("%s.%s which annotated be BindImageView must be String!!!", dataClazz.getSimpleName(), field.getName()));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }

                @Override
                public void onRecycled() {

                }
            };
        }
    }
}
