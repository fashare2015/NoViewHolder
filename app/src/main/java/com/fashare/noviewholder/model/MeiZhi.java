/*
 *
 * Copyright (C) 2015 Drakeet gmail.com>
 * Copyright (C) 2015 GuDong <maoruibin9035@gmail.com>
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.fashare.noviewholder.model;

import com.fashare.no_view_holder.annotation.BindImageView;
import com.fashare.no_view_holder.annotation.BindTextView;
import com.fashare.noviewholder.R;

import java.util.Date;

/**
 * 图片类
 * Created by Dimon on 2016/3/26.
 */
public class MeiZhi {

    /**
     *  _id : 58c72e86421aa90efc4fb6c5
     *  source : chrome
     *  "createdAt": "2016-03-24T12:35:23.841Z",
     *  "desc": "3.24",
     *  "publishedAt": "2016-03-25T11:23:49.570Z",
     *  "source": "chrome",
     *  "type": "福利",
     *  "url": "http://ww1.sinaimg.cn/large/7a8aed7bjw1f27uhoko12j20ez0miq4p.jpg",
     *  "used": true,
     *  "who": "张涵宇"
     */

    public boolean used;
    public String type;
    @BindImageView(id=R.id.iv_image, placeHolder = R.mipmap.ic_launcher)
    public String url;
    public String who;
    @BindTextView(id=R.id.tv_title)
    public String desc = "分类";
    public Date createdAt;
    public Date publishedAt;
    private String _id;
    private String source;

    @Override
    public String toString() {
        return desc;
    }
}
