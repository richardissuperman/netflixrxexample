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

/**
 *  mock services to retrieve data
 * **/
public class VideoService {

    /** a service for getting video objects by user id **/
    public static Observable<Video> getVideoObservableByUserId(String userid){
        return  Observable.create(new Observable.OnSubscribe<Video>() {
            @Override
            public void call(Subscriber<? super Video> subscriber) {
                try {
                    String response = new Query(userid).connect();
                    /** parse the video objects from json string and emit them one by one, noted that by using this
                     * API call, the video objects are incomplete ones, which need rating, meta data, and bookmark
                     * **/
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
                /** run the source in IO thread pool **/
                .subscribeOn(Schedulers.io());

    }


    /**  **/
    public static Observable<MetaData> getMetaDataObservable( String videoId ){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String response = new MetaDataQuery(videoId).connect();
                subscriber.onNext( response );
            }
        })
                .map(new Func1<String, MetaData>() {
                    @Override
                    public MetaData call(String s) {
                        return new MetaData(s);
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    public static Observable<BookMark> getBookmarkObservable( String videoId ){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String response = new BookmarkQuery(videoId).connect();
                subscriber.onNext( response );
            }
        })
                .map(new Func1<String, BookMark>() {
                    @Override
                    public BookMark call(String s) {
                        return new BookMark(s);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Rating> getRatingObservable( String videoId ){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String response = new RatingQuery(videoId).connect();
                    subscriber.onNext(response);
                }
                catch ( Exception e ){
                    subscriber.onError(e);
                }
            }
        })
                .map(new Func1<String, Rating>() {
                    @Override
                    public Rating call(String response) {
                        return new Rating(response);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

}
