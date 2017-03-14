package com.fashare.noviewholder.model;

import com.fashare.no_view_holder.annotation.BindImageView;
import com.fashare.no_view_holder.annotation.BindImageViews;
import com.fashare.no_view_holder.annotation.BindRecyclerView;
import com.fashare.no_view_holder.annotation.BindRvHeader;
import com.fashare.no_view_holder.annotation.BindViewPager;
import com.fashare.no_view_holder.annotation.LayoutManager;
import com.fashare.noviewholder.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-03-14
 * Time: 22:22
 * <br/><br/>
 */
public class HomeInfo {
    // recyclerView header 的 type, 根据它区分头部并排序
    interface ItemType{
        int BANNER = 0;
        int CLASSIFY = 1;
        int ON_SALE = 2;
    }

    // 主RecyclerView: 妹子列表区 (2列)
    @BindRecyclerView(id = R.id.rv_meizhi, layout = R.layout.item_meizhi,
            layoutManager = @LayoutManager(style = LayoutManager.Style.STAGGERED_GRID, spanCount = 2))
    private List<MeiZhi> results = new ArrayList<>();

    // 下边全是 Rv[rv_meizhi] 的头部, 动态加进去, 用 itemType 区分
    // banner
    @BindRvHeader(id = R.id.rv_meizhi, layout = R.layout.layout_banner, itemType = ItemType.BANNER)
    @BindViewPager(id = R.id.vp_banner, layout = R.layout.item_banner)
    private List<MeiZhi> bannerInfo;

    // 分类 (还是 RecyclerView, 然而作为头部, 嵌套在 Rv[rv_meizhi] 里边)
    @BindRvHeader(id = R.id.rv_meizhi, layout = R.layout.layout_classify, itemType = ItemType.CLASSIFY)
    @BindRecyclerView(id = R.id.rv_in_header, layout = R.layout.item_classify,
            layoutManager = @LayoutManager(style = LayoutManager.Style.GRID, spanCount = 4))    // GridLayout
    private List<MeiZhi> classifyInfo = Collections.nCopies(8, new MeiZhi());

    // 促销 (一些杂图, 依然作为头部, 嵌套在 Rv[rv_meizhi] 里边)
    @BindRvHeader(id = R.id.rv_meizhi, layout = R.layout.layout_on_sale, itemType = ItemType.ON_SALE)
    @BindImageViews({
            @BindImageView(id=R.id.onsale_iv1, placeHolder = R.mipmap.ic_launcher),
            @BindImageView(id=R.id.onsale_iv2, placeHolder = R.mipmap.ic_launcher),
            @BindImageView(id=R.id.onsale_iv3, placeHolder = R.mipmap.ic_launcher)
    })
    private List<String> onSaleInfo = Arrays.asList(
            "http://ww3.sinaimg.cn/large/610dc034jw1f8uxlbptw7j20ku0q1did.jpg",
            "http://ww1.sinaimg.cn/large/610dc034gw1fa1vkn6nerj20u011gdjm.jpg",
            "http://ww2.sinaimg.cn/large/610dc034jw1f978bh1cerj20u00u0767.jpg"
    );

    // 当当当当~~~, 一个简易的首页完成啦 !!!

    // 以下可忽略... getter(), setter() ...
    private boolean error;

    public List<MeiZhi> getResults() {
        return results;
    }

    public void setBannerInfo(List<MeiZhi> bannerInfo) {
        this.bannerInfo = bannerInfo;
    }
}