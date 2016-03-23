package com.example.zhongqing.learnrxjavasourcecode;

import com.example.zhongqing.learnrxjavasourcecode.beans.BookMark;
import com.example.zhongqing.learnrxjavasourcecode.beans.MetaData;
import com.example.zhongqing.learnrxjavasourcecode.beans.Rating;
import com.example.zhongqing.learnrxjavasourcecode.beans.Video;
import com.example.zhongqing.learnrxjavasourcecode.query.BookmarkQuery;
import com.example.zhongqing.learnrxjavasourcecode.query.MetaDataQuery;
import com.example.zhongqing.learnrxjavasourcecode.query.Query;
import com.example.zhongqing.learnrxjavasourcecode.query.RatingQuery;

import org.json.JSONArray;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhongqing on 22/3/16.
 */
public class VideoService {
    public static Observable<Video> getVideoObservableByUserId(String userid){
        return  Observable.create(new Observable.OnSubscribe<Video>() {
            @Override
            public void call(Subscriber<? super Video> subscriber) {
                try {
                    /** 同步方法获取api call返回值 **/
                    String response = new Query(userid).connect();
                    /** 解析jsonarray成一个个Video对象,并且一个个传入onnext ,此时的video对象是不完整的**/
                    JSONArray array = new JSONArray(response);
                    for( int i = 0 ; i<array.length() ;i++ ){
                        subscriber.onNext( Video.getVideoFromJson( array.getJSONObject(i) ) );
                    }
                }
                catch ( Exception e ){
                    subscriber.onError(e);
                }
            }
        })
                /** call运行在io线程上 **/
                .subscribeOn(Schedulers.io());

    }


    public static Observable<MetaData> getMetaDataObservable( String videoId ){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                /** 同步方法获取metadata返回值 **/
                String response = new MetaDataQuery(videoId).connect();
                subscriber.onNext( response );
            }
        })
                /** 用map把api call的返回值转换成MetaData **/
                .map(new Func1<String, MetaData>() {
                    @Override
                    public MetaData call(String s) {
                        return new MetaData(s);
                    }
                })
                /** call运行在io线程上 **/
                .subscribeOn(Schedulers.io());
    }


    public static Observable<BookMark> getBookmarkObservable( String videoId ){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                /** 同步方法获取bookmark返回值 **/
                String response = new BookmarkQuery(videoId).connect();
                subscriber.onNext( response );
            }
        })
                /** 用map把api call的返回值转换成MetaData **/
                .map(new Func1<String, BookMark>() {
                    @Override
                    public BookMark call(String s) {
                        return new BookMark(s);
                    }
                })
                /** call运行在io线程上 **/
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Rating> getRatingObservable( String videoId ){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    /** 同步方法获取bookmark返回值 **/
                    String response = new RatingQuery(videoId).connect();
                    subscriber.onNext(response);
                }
                catch ( Exception e ){
                    subscriber.onError(e);
                }
            }
        })
                /** 用map把api call的返回值转换成MetaData **/
                .map(new Func1<String, Rating>() {
                    @Override
                    public Rating call(String response) {
                        return new Rating(response);
                    }
                })
                /** call运行在io线程上 **/
                .subscribeOn(Schedulers.io());
    }

}
