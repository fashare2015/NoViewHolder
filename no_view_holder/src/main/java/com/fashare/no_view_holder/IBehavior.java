package com.fashare.no_view_holder;

import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;

import com.fashare.no_view_holder.annotation.BindTextView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-03-09
 * Time: 20:43
 *
 * 所有注解行为的基类.
 *
 * 1. 一个注解唯一对应一个 IBehavior.
 *    Tips: {@link NoViewHolder.Options#getMergedBehaviors()} 中做了去重处理.
 *
 * 2. 你可以自定义想要的 @BindXXX. 然后实现一个 IBehavior: {@link IBehavior.Simple}
 *
 * 3. 通过 {@link NoViewHolder.Options#setBehaviors(IBehavior[])}, 来配置你自定义的注解.
 *
 * @param <A> 该行为对应的注解, 如 {@link BindTextView}
 */
public interface IBehavior<A extends Annotation> {

    A getAnnotation(Field field);

    Class<A> belongsTo();

    /**
     * "该行为" 是否作用在 field 上. (即 field 是否被 "该行为" 相应的 @BindXXX 所注解)
     * @param field
     * @return
     */
    boolean isAppliedOn(Field field);

    /**
     * 初始化 View、Adapter、ClickListener
     *
     * @param noViewHolder
     * @param field
     * @param dataHolder
     */
    void onInitView(NoViewHolder noViewHolder, Field field, Object dataHolder);

    /**
     * 绑定数据
     *
     * @param noViewHolder
     * @param field
     * @param dataHolder
     */
    void onBind(NoViewHolder noViewHolder, Field field, Object dataHolder);

    /**
     * 封装了核心逻辑
     *
     * @param <A> 该行为对应注解的类型, 如 BindImageView
     * @param <DATA> 被注解 field 的合法类型, 如 BindImageView 需要 url 值, 合法类型为 String
     */
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
        public boolean isAppliedOn(Field field) {
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
        public final void onInitView(final NoViewHolder noViewHolder, Field field, final Object dataHolder) {
            checkFeild(field, dataHolder, new Action2<A, DATA>() {
                @Override
                public void call(A annotation, DATA value) {
                    onInitViewAbstract(noViewHolder, annotation, value, dataHolder);
                }
            });
        }

        @Override
        public final void onBind(final NoViewHolder noViewHolder, Field field, final Object dataHolder) {
            checkFeild(field, dataHolder, new Action2<A, DATA>() {
                @Override
                public void call(A annotation, DATA value) {
                    onBindAbstract(noViewHolder, annotation, value, dataHolder);
                }
            });
        }

        protected abstract void onInitViewAbstract(NoViewHolder noViewHolder, A annotation, DATA value, Object dataHolder);

        protected abstract void onBindAbstract(NoViewHolder noViewHolder, A annotation, DATA value, Object dataHolder);
    }

    /**
     * 大部分 @BindXXX.Behavior 的父类
     *
     * @param <A> 该行为对应注解的类型, 如 BindImageView
     * @param <V> 该行为相关的 View, 如 ImageView
     * @param <DATA> 被注解 field 的合法类型, 如 BindImageView 需要 url 值, 合法类型为 String
     */
    abstract class Simple<A extends Annotation, V extends View, DATA> extends Abstract<A, DATA> {
        public Simple(Class<A> annotationClazz, Class<DATA> valueClazz) {
            super(annotationClazz, valueClazz);
        }

        @Override
        final protected void onInitViewAbstract(NoViewHolder noViewHolder, A annotation, DATA value, Object dataHolder) {
            initViewIfNotNull(noViewHolder, (V) noViewHolder.itemView.findViewById(getId(annotation)), annotation, value, dataHolder);
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
        final protected void onBindAbstract(NoViewHolder noViewHolder, A annotation, DATA value, Object dataHolder) {
            bindIfNotNull(noViewHolder, (V) noViewHolder.itemView.findViewById(getId(annotation)), annotation, value, dataHolder);
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
        protected final void onInitViewAbstract(NoViewHolder noViewHolder, A annotation, DATA value, Object dataHolder) {
            onInitView(noViewHolder, noViewHolder.itemView, annotation, value);
        }

        protected void onInitView(NoViewHolder noViewHolder, View targetView, A annotation, DATA value){}

        @Override
        protected final void onBindAbstract(NoViewHolder noViewHolder, A annotation, DATA value, Object dataHolder) {
            onBind(noViewHolder, noViewHolder.itemView, annotation, value);
        }

        protected void onBind(NoViewHolder noViewHolder, View targetView, A annotation, DATA value){}
    }
}
