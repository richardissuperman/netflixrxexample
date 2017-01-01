package com.example.zhongqing.learnrxjavasourcecode.query;

/**
 * Created by zhongqing on 20/3/16.
 */
public class Query {
    public  Query(String s){

    }

    /** we use a fake implementation here, imagine replace it with any HTTP call by using OKHttp or other **/
    public String connect(){
        try {
            Thread.sleep(1000);
        }
        catch ( Exception e ){

        }
        return "response";
    }
}
