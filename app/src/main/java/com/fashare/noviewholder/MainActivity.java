package com.fashare.noviewholder;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fashare.no_view_holder.NoViewHolder;
import com.fashare.no_view_holder.annotation.BindTextView;
import com.fashare.no_view_holder.annotation.click.BindClick;
import com.fashare.no_view_holder.annotation.click.BindItemClick;
import com.fashare.no_view_holder.widget.OnItemClickListener;
import com.fashare.no_view_holder.widget.rv.wrapper.LoadMoreWrapper;
import com.fashare.noviewholder.model.ArticlePreview;
import com.fashare.noviewholder.model.LatestNews;
import com.fashare.noviewholder.model.TopArticle;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    static NoViewHolder.Options mDataOptions = new NoViewHolder.DataOptions()
            .setBehaviors(new BindTextView.Behavior() {
                @Override
                public void onBind(TextView targetView, BindTextView annotation, String value) {
                    targetView.setText("hahaha" + value);
                }
            });

    static {
        NoViewHolder.setDataOptions(mDataOptions);
    }

    NoViewHolder mNoViewHolder;

    LatestNews mLatestNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mNoViewHolder = new NoViewHolder.Factory(this)
                .initView(new LatestNews())
                .build();


        mSrlRefresh.setOnRefreshListener(reload);
        loadData();

//        ((LoadMoreWrapper) mRvArticleList.getAdapter()).setOnLoadMoreListener(loadMore);
    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(Api.class)
                .getLatestNewsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LatestNews>() {
                    @Override
                    public void call(LatestNews latestNews) {
                        mSrlRefresh.setRefreshing(false);
                        mNoViewHolder.notifyDataSetChanged(mLatestNews = latestNews);
                    }
                });
    }

    @BindView(R.id.rv_article_list)
    RecyclerView mRvArticleList;

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout mSrlRefresh;

    SwipeRefreshLayout.OnRefreshListener reload = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Toast.makeText(MainActivity.this, "reload", Toast.LENGTH_SHORT).show();
            loadData();
        }
    };


//    @BindLoadMore(id = R.id.rv_article_list, layout = R.layout.layout_load_more)
    LoadMoreWrapper.OnLoadMoreListener loadMore = new LoadMoreWrapper.OnLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLatestNews.getStories().add(new ArticlePreview());
                    mNoViewHolder.notifyDataSetChanged(mLatestNews);
                }
            }, 2000);
        }
    };

    @BindItemClick(id = R.id.vp_banner)
    OnItemClickListener<TopArticle> mOnTopArticleClicked = new OnItemClickListener<TopArticle>() {
        @Override
        public void onItemClick(View itemView, TopArticle data, int position) {
            Toast.makeText(MainActivity.this, "click TopArticle" + position, Toast.LENGTH_SHORT).show();
        }
    };

    @BindItemClick(id = R.id.rv_article_list)
    OnItemClickListener<ArticlePreview> mOnArticlePreviewClicked = new OnItemClickListener<ArticlePreview>() {
        @Override
        public void onItemClick(View itemView, ArticlePreview data, int position) {
            Toast.makeText(MainActivity.this, "click ArticlePreview: " + position + ", "+ data, Toast.LENGTH_SHORT).show();
        }
    };

    @BindItemClick(id = R.id.rv_in_header)
    OnItemClickListener<ArticlePreview> mOnRvInHeaderClicked = new OnItemClickListener<ArticlePreview>() {
        @Override
        public void onItemClick(View itemView, ArticlePreview data, int position) {
            Toast.makeText(MainActivity.this, "click rv_in_header" + position, Toast.LENGTH_SHORT).show();
        }
    };

    @BindClick(id = R.id.tv)
    View.OnClickListener mOnTvClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "click tv", Toast.LENGTH_SHORT).show();
        }
    };

    @BindClick(id = R.id.iv)
    View.OnClickListener mOnIvClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "click iv", Toast.LENGTH_SHORT).show();
        }
    };
}
