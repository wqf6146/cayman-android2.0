package com.spark.szhb_master.utils.okhttp.put;

import com.spark.szhb_master.utils.okhttp.RequestBuilder;
import com.spark.szhb_master.utils.okhttp.RequestCall;
import com.spark.szhb_master.utils.okhttp.get.GetRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/11/13.
 */

public class PutBuilder extends RequestBuilder {
    @Override
    public RequestCall build() {
        return new PutRequest(url, params, headers).build();
    }


    @Override
    public PutBuilder url(String url) {
        this.url = url;
        return this;
    }


    @Override
    public PutBuilder addParams(String key, String val) {
        if (this.params == null) params = new HashMap<>();
        params.put(key, val);
        return this;
    }

    @Override
    public PutBuilder addHeader(String key, String value) {
        if (this.headers == null) headers = new HashMap<>();
        headers.put(key, value);
        return this;
    }

    @Override
    public RequestBuilder addParams(Map<String, String> map) {
        if (this.params == null) params = new HashMap<>();
        params.putAll(map);
        return this;
    }
}
