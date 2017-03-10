package com.fashare.noviewholder.model;

import com.fashare.no_view_holder.annotation.BindImageView;
import com.fashare.noviewholder.R;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-09-07
 * Time: 20:01
 * <br/><br/>
 */
public class TopArticle  {
    /**
     * title : 商场和很多人家里，竹制家具越来越多（多图）
     * image : http://p2.zhimg.com/9a/15/9a1570bb9e5fa53ae9fb9269a56ee019.jpg
     * ga_prefix : 052315
     * type : 0
     * id : 3930883
     */

    @BindImageView(id = R.id.iv_image, placeHolder = R.drawable.banner_default)
    private String image;

    private String title;
    private String ga_prefix;
    private int type;
    private int id;
}
