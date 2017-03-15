# 写在前面
> 我们的目标是 No ViewHolder and No Adapter.

官方的`databinding`的确十分厉害，各种`xml`绑定，然后自动生成一波文件，各种吊的飞起，不过容易让人抓不住重点。为了加深理解，我写了这个`纯java版`的`databindng`, 不需要`xml`各种配置`android:text="@{...}"`，同时进一步加了绑定`Adapter`。

>时间仓促，只粗略的实现了小部分功能。基于注解的性能也有待优化，但它已经极大地提升了我的开发效率。觉得它不错的话，可以一起维护这个项目，向`No ViewHolder`的目标迈进~


# 预览
### 常规的电商首页

![](https://raw.githubusercontent.com/fashare2015/NoViewHolder/master/screen-record/preview.gif)

### 所需代码量
实现这样一个带`Header`, 带`上拉加载`的列表需要多少代码呢？
- 两个无聊的 javabean
- 一个轮播控件
- 一个Activity（真的不含 Adapter 啊）
- 然后没有然后了。。。

![](https://raw.githubusercontent.com/fashare2015/NoViewHolder/master/screen-record/project.png)

# 特性
- `data -> view`的单向绑定
- 支持常用控件的绑定，同时增加了官方没有的`Adapter`绑定。支持`Header`和`上拉加载`
- 代码极其简洁, 无需实例化`View`, 也没有`Adapter`, 连`ViewHoler` 也没有。。。
- 支持绑定行为的自定义
- 配合 Rxjava + Lambda 简直上天

# gralde 依赖
```gradle
// 1. Add it in your root build.gradle at the end of repositories:
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

// 2. Add the dependency in your app/build.gradle
dependencies {
        compile 'com.github.fashare2015:NoViewHolder:1.0.1'
}
```
# 绑定 Data 和 View
这一块和官方差不多，只是`xml`配置换成了`java注解`配置。

## 绑定单个 View
首先，你手头有一个`javabean`，就是你在图中看到的`妹子列表Item`如：
```java
public class MeiZhi {
    @BindImageView(id=R.id.iv_image, placeHolder = R.mipmap.ic_launcher)
    public String url;  // 把 url 绑定在 ImageView 上
    @BindTextView(id=R.id.tv_title)
    public String desc; // 把 desc 绑定在 TextView 上
}
```
基本等同于官方的`android:text="@{meizhi.desc}"`，用过`databinding`的话应该秒懂的。。。


## 绑定列表
当然，服务端返回的肯定是个妹子的列表，你手头还会有一个`HomeInfo`的东东。
```java
public class HomeInfo {
    // 妹子列表区
    @BindRecyclerView(id = R.id.rv_meizhi, layout = R.layout.item_meizhi)
    private List<MeiZhi> results = new ArrayList<>();   // 把 List 绑定在 RecyclerView 上

    // banner
    @BindViewPager(id = R.id.vp_banner, layout = R.layout.item_banner)
    private List<MeiZhi> bannerInfo;    // 把 List 绑定在 ViewPager 上
}
```
这部分是官方没有的，相应的还提供了 `@BindListView`

## 绑定 header
像上面的配置，`banner`和妹子列表是分开的，不会一起滑动的。因此，提供了向`RecyclerView`中添加`Header`的注解——`@BindRvHeader`.
让我们把`banner`加进`RecyclerView`

```java
public class HomeInfo {
    // 妹子列表区
    @BindRecyclerView(id = R.id.rv_meizhi, layout = R.layout.item_meizhi)
    private List<MeiZhi> results = new ArrayList<>();   // 把 List 绑定在 RecyclerView 上

    // banner
    @BindRvHeader(id = R.id.rv_meizhi, layout = R.layout.layout_banner, itemType = 0) // 增加这一行 !!!
    @BindViewPager(id = R.id.vp_banner, layout = R.layout.item_banner)
    private List<MeiZhi> bannerInfo;    // 把 List 绑定在 ViewPager 上
}
```

## 绑定点击事件
提供了`@BindItemClick`、`@BindClick`
```java
public class MainActivity extends AppCompatActivity {
    ...
    @BindItemClick(id = R.id.vp_banner)
    NoOnItemClickListener<MeiZhi> clickBanner = (view, data, pos) -> toast("click Banner: " + pos + ", "+ data.toString());

    @BindItemClick(id = R.id.rv_meizhi)
    NoOnItemClickListener<MeiZhi> clickMeiZhi = (view, data, pos) -> toast("click MeiZhi: " + pos + ", "+ data.toString());
}
```

# 更新 UI
前面只是一系列绑定关系的配置，还需要一个接口触发他们：
- 初始化：根据 R.id.XXX 初始化相应的 View 和 Adapter，为后续`更新UI`做准备

```java
mNoViewHolder = new NoViewHolder.Builder(this)
                .initView(new HomeInfo()) // 一定要提供`注解信息`的类，否则无法初始化。
                .build();
```
- 更新UI: `mNoViewHolder.notifyDataSetChanged(homeInfo);`
自动根据 `homeInfo` 里提供的注解信息，找到相应的控件，并把数据刷新上去。

```java
// 在请求的 onSuccess() 中刷新界面，本例使用了 Rxjava 和 lambda
homeInfoObservable.subscribe(homeInfo -> {
        mHomeInfo.getResults().addAll(homeInfo.getResults());           // 更新 妹子列表 info
        if(homeInfo.getResults().size() >= 6)
            mHomeInfo.setBannerInfo(homeInfo.getResults().subList(0, 6));   // 更新 bannerInfo

        mNoViewHolder.notifyDataSetChanged(mHomeInfo);  // mHomeInfo 发生变化, 通知 UI 及时刷新
}
```

# 全局配置——自定义行为
当你需要自定义的时候 (比如替换图片加载库，默认`Glide`)。可以这样：
如下，即把`@BindTextView`的行为`override`掉了。
```java
    static NoViewHolder.Options mDataOptions = new NoViewHolder.DataOptions()
            .setBehaviors(new BindTextView.Behavior() {
                @Override
                public void onBind(TextView targetView, BindTextView annotation, Object value) {
                    targetView.setText("fashare 到此一游" + value);
                }
            });

    static {
        NoViewHolder.setDataOptions(mDataOptions);
    }
```

# 总结
水平有限，实现的比较粗糙。但我觉得这个思路还行，用起来简洁性也丝毫不比官方的差。觉得它不错的话，可以一起维护这个项目，向`No ViewHolder`的目标迈进~

# 一些类似实现
https://github.com/Kelin-Hong/MVVMLight

https://github.com/evant/binding-collection-adapter

# 感谢
https://github.com/hongyangAndroid/baseAdapter (基于它封装的)

https://github.com/mcxtzhang/all-base-adapter

[完全掌握Android Data Binding](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0603/2992.html)

[[译]关于 Android Adapter，你的实现方式可能一直都有问题](http://www.jianshu.com/p/c6a44e18badb)
