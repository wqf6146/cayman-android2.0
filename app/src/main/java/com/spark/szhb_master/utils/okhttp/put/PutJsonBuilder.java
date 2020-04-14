package com.spark.szhb_master.utils.okhttp.put;

import com.spark.szhb_master.utils.okhttp.RequestBuilder;
import com.spark.szhb_master.utils.okhttp.RequestCall;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;


/**
 * Created by Administrator on 2017/11/13.
 */

public class PutJsonBuilder extends RequestBuilder {

    private String body;
    private MediaType mime;

    @Override
    public PutJsonBuilder url(String url) {
        this.url = url;
        return this;
    }

    public PutJsonBuilder body(String body) {
        this.body = body;
        return this;
    }

    public PutJsonBuilder mime(MediaType mime) {
        this.mime = mime;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PutJsonRequest(url, params, headers, body, mime).build();
    }

    @Override
    public PutJsonBuilder addParams(String key, String val) {
        if (this.params == null) params = new HashMap<>();
        params.put(key, val);
        return this;
    }

    @Override
    public PutJsonBuilder addHeader(String key, String val) {
        if (this.headers == null) headers = new HashMap<>();
        headers.put(key, val);
        return this;
    }

    @Override
    public RequestBuilder addParams(Map<String, String> map) {
        return this;
    }
}
