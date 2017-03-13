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
    A getAnnotation(Field field);

    Class<A> belongsTo();

    boolean isApplyedOn(Field field);

    void onInitView(NoViewHolder noViewHolder, View itemView, Field field, Object dataHolder);

    void onBind(NoViewHolder noViewHolder, View itemView, Field field, Object dataHolder);


    abstract class Abstract<A extends Annotation, DATA> implements IBehavior<A> {
        protected final String TAG = this.getClass().getCanonicalName();
        final Class<A> mAnnotationClazz;
        final Class<DATA> mValueClazz;

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

        public Abstract(Class<A> annotationClazz, Class<DATA> valueClazz) {
            mAnnotationClazz = annotationClazz;
            mValueClazz = valueClazz;
        }

        @Override
        public boolean isApplyedOn(Field field) {
            return getAnnotation(field) != null;
        }

        interface Action2<A, DATA>{
            void call(A annotation, DATA value);
        }

        private void checkFeild(Field field, Object dataHolder, Action2<A, DATA> callback) {
            A annotation = getAnnotation(field);
            try {
                field.setAccessible(true);
                Object value = field.get(dataHolder);

                if(mValueClazz.isAssignableFrom(field.getType())) {
                    Log.d(TAG, field.getName() + ": " + (value==null? "null": value.toString()));

                    if(callback != null)
                        callback.call(annotation, (DATA)value);
                }else
                    throw new IllegalStateException(String.format("%s.%s which annotated by %s must be %s!!!",
                            dataHolder.getClass().getSimpleName(), field.getName(), mAnnotationClazz.getSimpleName(), mValueClazz.getSimpleName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public final void onInitView(final NoViewHolder noViewHolder, final View itemView, Field field, final Object dataHolder) {
            checkFeild(field, dataHolder, new Action2<A, DATA>() {
                @Override
                public void call(A annotation, DATA value) {
                    onInitViewAbstract(noViewHolder, itemView, annotation, value, dataHolder);
                }
            });
        }

        @Override
        public final void onBind(final NoViewHolder noViewHolder, final View itemView, Field field, final Object dataHolder) {
            checkFeild(field, dataHolder, new Action2<A, DATA>() {
                @Override
                public void call(A annotation, DATA value) {
                    onBindAbstract(noViewHolder, itemView, annotation, value, dataHolder);
                }
            });
        }

        protected abstract void onInitViewAbstract(NoViewHolder noViewHolder, View itemView, A annotation, DATA value, Object dataHolder);

        protected abstract void onBindAbstract(NoViewHolder noViewHolder, View itemView, A annotation, DATA value, Object dataHolder);
    }

    abstract class Simple<A extends Annotation, V extends View, DATA> extends Abstract<A, DATA> {
        public Simple(Class<A> annotationClazz, Class<DATA> valueClazz) {
            super(annotationClazz, valueClazz);
        }

        @Override
        final protected void onInitViewAbstract(NoViewHolder noViewHolder, View itemView, A annotation, DATA value, Object dataHolder) {
            initViewIfNotNull(noViewHolder, (V) itemView.findViewById(getId(annotation)), annotation, value, dataHolder);
        }

        public void initViewIfNotNull(NoViewHolder noViewHolder, V targetView, A annotation, DATA value, Object dataHolder) {
            if(targetView != null){
                onInitView(targetView, annotation, value);
                onInitView(targetView, annotation, value, dataHolder);
                onInitView(noViewHolder, targetView, annotation, value);
                onInitView(noViewHolder, targetView, annotation, value, dataHolder);
            }else{
                // TODO: getId() 不再打印纯数字!
                Log.e(TAG, String.format("%s with id(%d) is not found!!!", annotation.toString(), getId(annotation)));
            }
        }

        protected void onInitView(V targetView, A annotation, DATA value){}
        protected void onInitView(V targetView, A annotation, DATA value, Object dataHolder){}

        protected void onInitView(NoViewHolder noViewHolder, V targetView, A annotation, DATA value){}
        protected void onInitView(NoViewHolder noViewHolder, V targetView, A annotation, DATA value, Object dataHolder){}

        @Override
        final protected void onBindAbstract(NoViewHolder noViewHolder, View itemView, A annotation, DATA value, Object dataHolder) {
            bindIfNotNull(noViewHolder, (V) itemView.findViewById(getId(annotation)), annotation, value, dataHolder);
        }

        public void bindIfNotNull(NoViewHolder noViewHolder, V targetView, A annotation, DATA value, Object dataHolder) {
            if(targetView != null){
                onBind(targetView, annotation, value);
                onBind(targetView, annotation, value, dataHolder);
                onBind(noViewHolder, targetView, annotation, value);
                onBind(noViewHolder, targetView, annotation, value, dataHolder);
            }else{
                // TODO: getId() 不再打印纯数字!
                Log.e(TAG, String.format("%s with id(%d) is not found!!!", annotation.toString(), getId(annotation)));
            }
        }

        protected abstract @IdRes int getId(A annotation);

        protected void onBind(V targetView, A annotation, DATA value){}
        protected void onBind(V targetView, A annotation, DATA value, Object dataHolder){}

        protected void onBind(NoViewHolder noViewHolder, V targetView, A annotation, DATA value){}
        protected void onBind(NoViewHolder noViewHolder, V targetView, A annotation, DATA value, Object dataHolder){}
    }

    abstract class Group<A extends Annotation, DATA> extends Abstract<A, DATA>{
        public Group(Class<A> annotationClazz, Class<DATA> valueClazz) {
            super(annotationClazz, valueClazz);
        }

        @Override
        protected final void onInitViewAbstract(NoViewHolder noViewHolder, View itemView, A annotation, DATA value, Object dataHolder) {
            onInitView(noViewHolder, itemView, annotation, value);
        }

        protected void onInitView(NoViewHolder noViewHolder, View targetView, A annotation, DATA value){}

        @Override
        protected final void onBindAbstract(NoViewHolder noViewHolder, View itemView, A annotation, DATA value, Object dataHolder) {
            onBind(noViewHolder, itemView, annotation, value);
        }

        protected void onBind(NoViewHolder noViewHolder, View targetView, A annotation, DATA value){}
    }
}
