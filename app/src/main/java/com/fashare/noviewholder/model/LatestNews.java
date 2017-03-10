package com.fashare.noviewholder.model;

import com.fashare.no_view_holder.annotation.BindRecyclerView;
import com.fashare.no_view_holder.annotation.BindViewPager;
import com.fashare.noviewholder.R;

import java.util.List;

/**
 * Created by jinliangshan on 16/8/26.
 */
public class LatestNews{

    /**
     * date : 20140523
     * stories : [{"title":"中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）","ga_prefix":"052321","images":["http://p1.zhimg.com/45/b9/45b9f057fc1957ed2c946814342c0f02.jpg"],"type":0,"id":3930445}]
     * top_stories : [{"title":"商场和很多人家里，竹制家具越来越多（多图）","image":"http://p2.zhimg.com/9a/15/9a1570bb9e5fa53ae9fb9269a56ee019.jpg","ga_prefix":"052315","type":0,"id":3930883}]
     */

    @BindViewPager(id = R.id.vp_banner, layout = R.layout.item_banner)
    private List<TopArticle> top_stories;

    @BindRecyclerView(id = R.id.rv_article_list, layout = R.layout.item_article)
    private List<ArticlePreview> stories;

    private String date = "";
}
