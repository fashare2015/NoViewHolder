package com.fashare.noviewholder.model;

import com.fashare.no_view_holder.annotation.BindImageView;
import com.fashare.no_view_holder.annotation.BindImageViews;
import com.fashare.no_view_holder.annotation.BindTextView;
import com.fashare.noviewholder.R;

import java.util.List;

/**
 * Created by jinliangshan on 16/8/25.
 *
 *
 */
public class ArticlePreview {
    /**
     * title : 中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）
     * ga_prefix : 052321
     * images : ["http://p1.zhimg.com/45/b9/45b9f057fc1957ed2c946814342c0f02.jpg"]
     * type : 0
     * id : 3930445
     */

    @BindTextView(id=R.id.tv_title)
    private String title;

    @BindImageViews({
            @BindImageView(id=R.id.iv_image, placeHolder=R.mipmap.ic_launcher)
    })
    private List<String> images;

    private String ga_prefix;
    private int type;
    private int id;

    @Override
    public String toString() {
        return title;
    }
}
