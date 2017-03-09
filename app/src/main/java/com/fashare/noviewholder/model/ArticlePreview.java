package com.fashare.noviewholder.model;

import com.fashare.no_view_holder.annotation.BindImageView;
import com.fashare.no_view_holder.annotation.BindTextView;
import com.fashare.noviewholder.R;

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
    private String title = "中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）";
    private String ga_prefix;
    private int type;
    private int id;
    @BindImageView(id=R.id.iv_image, placeHolder=R.mipmap.ic_launcher)
    private String image = "http://p1.zhimg.com/45/b9/45b9f057fc1957ed2c946814342c0f02.jpg";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
