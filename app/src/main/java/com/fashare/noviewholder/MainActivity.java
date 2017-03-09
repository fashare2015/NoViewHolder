package com.fashare.noviewholder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.fashare.no_view_holder.ImageLoader;
import com.fashare.noviewholder.model.ArticlePreview;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final List DATA = Arrays.asList(
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview()
    );

    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.iv)
    ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        NoRecyclerViewAdapter adapter;
        mRv.setAdapter(adapter = new NoRecyclerViewAdapter(this, R.layout.item_article));
        mRv.setLayoutManager(new LinearLayoutManager(this));

        adapter.setDataList(DATA);

        ImageLoader.loadImage(mIv, "http://p1.zhimg.com/45/b9/45b9f057fc1957ed2c946814342c0f02.jpg", R.mipmap.ic_launcher);
    }
}
