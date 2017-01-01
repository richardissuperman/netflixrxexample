package com.example.zhongqing.learnrxjavasourcecode.beans;

import com.example.zhongqing.learnrxjavasourcecode.VideoService;

import org.json.JSONObject;

import rx.Observable;

/**
 * Created by zhongqing on 22/3/16.
 */
public class Video {
    public String id;
    public String otherData;


    public MetaData metaData;
    public Rating rating;
    public BookMark bookMark;




    public Video(){
        //do something
    }
    /** just ignore the implementation in this sample XD **/
    public Video(JSONObject object){
        //do something
    }

    public Observable<MetaData> getMetaDataObservable(){
        return VideoService.getMetaDataObservable(this.id);
    }
    public Observable<BookMark> getBookmarObservable(){
        return VideoService.getBookmarkObservable(this.id);
    }
    public Observable<Rating> getRatingObservable(){
        return VideoService.getRatingObservable( this.id );
    }



    public static Video getVideoFromJson(JSONObject object){
        return new Video( object );
    }
}
