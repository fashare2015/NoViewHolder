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

    Class<A> belongsTo();

    abstract class Simple<A extends Annotation, V extends View, DATA> implements IBehavior<A> {
        protected final String TAG = this.getClass().getCanonicalName();
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
        public final void onBind(View itemView, Field field, Object dataHolder) {
            A annotation = getAnnotation(field);
            try {
                field.setAccessible(true);
                Object value = field.get(dataHolder);

                if(mValueClazz.isAssignableFrom(value.getClass())) {
                    Log.d(TAG, value.toString());
//                    onBind(itemView, annotation, mValueClazz.cast(value));

                    bindIfNotNull((V) itemView.findViewById(getId(annotation)), annotation, (DATA)value, dataHolder);
                }else
                    throw new IllegalStateException(String.format("%s.%s which annotated by %s must be %s!!!",
                            dataHolder.getClass().getSimpleName(), field.getName(), mAnnotationClazz.getSimpleName(), mValueClazz.getSimpleName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        protected abstract @IdRes int getId(A annotation);

        public abstract void onBind(V targetView, A annotation, DATA value);

        protected void onBind(V targetView, A annotation, DATA value, Object dataHolder){}

        public void bindIfNotNull(V targetView, A annotation, DATA value, Object dataHolder) {
            if(targetView != null){
                onBind(targetView, annotation, value);
                onBind(targetView, annotation, value, dataHolder);
            }else{
                Log.e(TAG, String.format("%s with id(%d) is not found!!!", annotation.getClass().getSimpleName(), getId(annotation)));
            }
        }

        @Override
        public A getAnnotation(Field field) {
            return field.getAnnotation(mAnnotationClazz);
        }

        @Override
        public Class<A> belongsTo() {
            return mAnnotationClazz;
        }

        // should override equals() and hashcode(), while using HashSet() on NoViewHolder.Options
        @Override
        public boolean equals(Object obj) {
            return obj instanceof IBehavior && this.belongsTo() == ((IBehavior) obj).belongsTo();
        }

        @Override
        public int hashCode() {
            return this.belongsTo().hashCode();
        }
    }

    abstract class Group<A extends Annotation, DATA> implements IBehavior<A>{
        Class<A> mAnnotationClazz;
        Class<DATA> mValueClazz;

        public Group(Class<A> annotationClazz, Class<DATA> valueClazz) {
            mAnnotationClazz = annotationClazz;
            mValueClazz = valueClazz;
        }

        @Override
        public boolean isApplyedOn(Field field) {
            return getAnnotation(field) != null;
        }

        @Override
        public final void onBind(View itemView, Field field, Object dataHolder) {
            A annotation = getAnnotation(field);
            try {
                field.setAccessible(true);
                Object value = field.get(dataHolder);

                if(mValueClazz.isAssignableFrom(value.getClass())) {
                    Log.d(this.getClass().getSimpleName(), value.toString());
//                    onBind(itemView, annotation, mValueClazz.cast(value));

                    onBind(itemView, annotation, (DATA)value);
                }else
                    throw new IllegalStateException(String.format("%s.%s which annotated by %s must be %s!!!",
                            dataHolder.getClass().getSimpleName(), field.getName(), mAnnotationClazz.getSimpleName(), mValueClazz.getSimpleName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public abstract void onBind(View itemView, A annotation, DATA value);

        @Override
        public A getAnnotation(Field field) {
            return field.getAnnotation(mAnnotationClazz);
        }

        @Override
        public Class<A> belongsTo() {
            return mAnnotationClazz;
        }

        // should override equals() and hashcode(), while using HashSet() on NoViewHolder.Options
        @Override
        public boolean equals(Object obj) {
            return obj instanceof IBehavior && this.belongsTo() == ((IBehavior) obj).belongsTo();
        }

        @Override
        public int hashCode() {
            return this.belongsTo().hashCode();
        }
    }
}
