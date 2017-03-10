package com.fashare.noviewholder;

import com.fashare.noviewholder.model.LatestNews;

import retrofit2.http.GET;
import rx.Observable;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-03-10
 * Time: 23:32
 * <br/><br/>
 */
public interface Api {
    String BASE_URL = "http://news.at.zhihu.com/api/4/";

    String URL_LATEST_NEWS = "news/latest";

    @GET(URL_LATEST_NEWS)
    Observable<LatestNews> getLatestNewsObservable();
}  