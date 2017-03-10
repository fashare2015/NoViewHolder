package com.fashare.no_view_holder;

import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-03-09
 * Time: 20:43
 * <br/><br/>
 */
public interface IBehavior<A extends Annotation> {
    boolean isApplyedOn(Field field);

    void onBind(View itemView, Field field, Object data);

    A getAnnotation(Field field);

    abstract class Simple<A extends Annotation, V extends View, DATA> implements IBehavior<A> {
        Class<A> mAnnotationClazz;
        Class<DATA> mValueClazz;

        public Simple(Class<A> annotationClazz, Class<DATA> valueClazz) {
            mAnnotationClazz = annotationClazz;
            mValueClazz = valueClazz;
        }

        @Override
        public boolean isApplyedOn(Field field) {
            return getAnnotation(field) != null;
        }

        @Override
        public final void onBind(View itemView, Field field, Object data) {
            A annotation = getAnnotation(field);
            try {
                field.setAccessible(true);
                Object value = field.get(data);

                if(mValueClazz.isAssignableFrom(value.getClass())) {
                    Log.d(this.getClass().getSimpleName(), value.toString());
//                    onBind(itemView, annotation, mValueClazz.cast(value));

                    bindIfNotNull((V) itemView.findViewById(getId(annotation)), annotation, (DATA)value);
                }else
                    throw new IllegalStateException(String.format("%s.%s which annotated by %s must be %s!!!",
                            data.getClass().getSimpleName(), field.getName(), mAnnotationClazz.getSimpleName(), mValueClazz.getSimpleName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        protected abstract @IdRes int getId(A annotation);

        public abstract void onBind(V targetView, A annotation, DATA value);

        protected void bindIfNotNull(V targetView, A annotation, DATA value) {
            if(targetView != null){
                onBind(targetView, annotation, value);
            }else{
                throw new IllegalStateException(String.format("%s with id(%d) is not found!!!", targetView.getClass().getSimpleName(), getId(annotation)));
            }
        }

        @Override
        public A getAnnotation(Field field) {
            return field.getAnnotation(mAnnotationClazz);
        }
    }
}
