package com.fashare.noviewholder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.fashare.no_view_holder.NoViewHolder;
import com.fashare.no_view_holder.annotation.BindImageView;
import com.fashare.no_view_holder.annotation.BindRecyclerView;
import com.fashare.no_view_holder.annotation.BindViewPager;
import com.fashare.no_view_holder.annotation.LayoutManager;
import com.fashare.no_view_holder.annotation.click.BindClick;
import com.fashare.no_view_holder.annotation.click.BindItemClick;
import com.fashare.no_view_holder.widget.OnItemClickListener;
import com.fashare.noviewholder.model.ArticlePreview;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @BindImageView(id = R.id.iv, placeHolder = R.mipmap.ic_launcher)
    String img="";

    @BindClick(id = R.id.iv)
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "click img", Toast.LENGTH_SHORT).show();
        }
    };

    @BindViewPager(
            id = R.id.vp,
            layout = R.layout.item_article
    )
    @BindRecyclerView(
            id = R.id.rv,
            layout = R.layout.item_article,
            layoutManager = @LayoutManager(
                    style = LayoutManager.Style.STAGGERED_GRID,
                    spanCount = 2
            )
    )
//    @BindListView(
//            id = R.id.lv,
//            layout = R.layout.item_article
//    )
    private static final List DATA = Arrays.asList(
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview(),
            new ArticlePreview()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoViewHolder.Factory.create(this).bind(this);
    }

    @BindItemClick(id = R.id.vp)
    OnItemClickListener mVpOnItemClick = new OnItemClickListener<ArticlePreview>() {
        @Override
        public void onItemClick(View itemView, ArticlePreview data, int position) {
            Toast.makeText(MainActivity.this, "pos="+position + ", data="+data.getTitle(), Toast.LENGTH_SHORT).show();
        }
    };

//    @BindItemClick(id = R.id.lv)
//    OnItemClickListener mLvOnItemClick = new OnItemClickListener<ArticlePreview>() {
//        @Override
//        public void onItemClick(View itemView, ArticlePreview data, int position) {
//            Toast.makeText(MainActivity.this, "pos="+position + ", data="+data.getTitle(), Toast.LENGTH_SHORT).show();
//        }
//    };

    @BindItemClick(id = R.id.rv)
    OnItemClickListener mRvOnItemClick = new OnItemClickListener<ArticlePreview>() {
        @Override
        public void onItemClick(View itemView, ArticlePreview data, int position) {
            Toast.makeText(MainActivity.this, "pos="+position + ", data="+data.getTitle(), Toast.LENGTH_SHORT).show();
        }
    };
}
