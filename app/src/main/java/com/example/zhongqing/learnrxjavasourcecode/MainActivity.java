package com.example.zhongqing.learnrxjavasourcecode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zhongqing.learnrxjavasourcecode.beans.BookMark;
import com.example.zhongqing.learnrxjavasourcecode.beans.MetaData;
import com.example.zhongqing.learnrxjavasourcecode.beans.Rating;
import com.example.zhongqing.learnrxjavasourcecode.beans.Video;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoService.getVideoObservableByUserId( "userId" )
                /** 获取前十个video **/
                .take(10)
                /** nested call back! **/
                .flatMap(new Func1<Video, Observable<Video>>() {
                    @Override
                    public Observable<Video> call(Video video) {

                        /** 获取前三个不同observable并且把他们merge到一起,合成新的video **/
                        Observable<BookMark> o1 = video.getBookmarObservable();
                        Observable<MetaData> o2 = video.getMetaDataObservable();
                        Observable<Rating> o3 = video.getRatingObservable();

                        return o1.zipWith(o2, new Func2<BookMark, MetaData, Video>() {
                             @Override
                             public Video call(BookMark s, MetaData s2) {
                                 video.bookMark = s;
                                 video.metaData = s2;
                                 return video;
                             }
                         })
                          .zipWith(o3, new Func2<Video, Rating, Video>() {
                              @Override
                              public Video call(Video video, Rating rating) {
                                  video.rating = rating;
                                  return video;
                              }
                          });
                    }
                })
                /** 在安卓主线程上接受发射的结果 **/
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Video>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Video video) {

                    }
                });
    }

}
