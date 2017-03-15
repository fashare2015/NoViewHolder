package com.fashare.noviewholder;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fashare.no_view_holder.NoViewHolder;
import com.fashare.no_view_holder.annotation.BindTextView;
import com.fashare.no_view_holder.annotation.click.BindClick;
import com.fashare.no_view_holder.annotation.click.BindItemClick;
import com.fashare.no_view_holder.annotation.click.BindLoadMore;
import com.fashare.no_view_holder.widget.NoOnItemClickListener;
import com.fashare.no_view_holder.widget.rv.wrapper.NoLoadMoreWrapper;
import com.fashare.noviewholder.model.HomeInfo;
import com.fashare.noviewholder.model.MeiZhi;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    // --- NoViewHolder 的 Behavior 全局配置 ---
    static NoViewHolder.Options mDataOptions = new NoViewHolder.DataOptions()
            .setBehaviors(new BindTextView.Behavior() {
                @Override
                public void onBind(TextView targetView, BindTextView annotation, Object value) {
                    targetView.setText("fashare 到此一游" + value);
                }
            });

    static {
//        NoViewHolder.setDataOptions(mDataOptions);
    }
    // --- NoViewHolder 的 Behavior 全局配置 ---

    // --- 一些不重要的变量 ---
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout mSrlRefresh;
    // --- 一些不重要的变量 ---

    // NoViewHolder !!! 所有 view 的容器
    NoViewHolder mNoViewHolder;

    int curPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mNoViewHolder = new NoViewHolder.Factory(this)
                .initView(new HomeInfo())
                .build();

        mSrlRefresh.setOnRefreshListener(() -> {
            mHomeInfo = new HomeInfo(); // 清空数据
            loadData(curPage = 0);
        });
    }

    // --- 网络请求 ---
    interface Api {
        String BASE_URL = "http://gank.io/api/";

        @GET("data/福利/"+10+"/{page}")
        Observable<HomeInfo> getHomeInfo(@Path("page") int page);
    }

    HomeInfo mHomeInfo = new HomeInfo();

    private void loadData(int page) {
        retrofit.create(Api.class)
                .getHomeInfo(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(homeInfo -> {
                        mSrlRefresh.setRefreshing(false);

                        mHomeInfo.getResults().addAll(homeInfo.getResults());           // 更新 妹子列表 info
                        if(homeInfo.getResults().size() >= 6)
                            mHomeInfo.setBannerInfo(homeInfo.getResults().subList(0, 6));   // 更新 bannerInfo

                        mNoViewHolder.notifyDataSetChanged(mHomeInfo);  // mHomeInfo 发生变化, 通知 UI 及时刷新

                }, throwable -> {
                        mSrlRefresh.setRefreshing(false);
                        toast("服务器跑路啦~");
                });
    }
    // --- 网络请求 ---

    @BindLoadMore(id = R.id.rv_meizhi, layout = R.layout.layout_load_more)
    NoLoadMoreWrapper.OnLoadMoreListener loadMore = () -> new Handler().postDelayed(() -> loadData(curPage++), 2000);

    @BindItemClick(id = R.id.vp_banner)
    NoOnItemClickListener<MeiZhi> clickBanner = (view, data, pos) -> toast("click Banner: " + pos + ", "+ data.toString());

    @BindItemClick(id = R.id.rv_meizhi)
    NoOnItemClickListener<MeiZhi> clickMeiZhi = (view, data, pos) -> toast("click MeiZhi: " + pos + ", "+ data.toString());

    @BindItemClick(id = R.id.rv_in_header)
    NoOnItemClickListener<MeiZhi> clickClissify = (view, data, pos) -> toast("click Clissify: " + pos + ", "+ data.toString());

    @BindClick(id = R.id.onsale_iv1)
    View.OnClickListener clickOnSale1 = v -> toast("click OnSale1");

    void toast(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
