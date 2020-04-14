package com.spark.szhb_master.utils.okhttp.put;


import com.spark.szhb_master.utils.okhttp.OkHttpRequest;

import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;


public class PutJsonRequest extends OkHttpRequest {

    private String body;
    private MediaType mime;

    protected PutJsonRequest(String url, Map params, Map<String, String> headers, String body, MediaType mime) {
        super(url, params, headers);
        this.body = body;
        this.mime = mime;
    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mime, body);
    }


    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.put(requestBody).build();
    }
}
