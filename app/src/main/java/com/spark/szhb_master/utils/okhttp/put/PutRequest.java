package com.spark.szhb_master.utils.okhttp.put;


import com.spark.szhb_master.utils.okhttp.OkHttpRequest;

import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/11/13.
 * 公司一直用的post 暂时没有封装 如果需要 请参考 洪洋的开源项目 做一些修改即可
 */

public class PutRequest extends OkHttpRequest {

    protected PutRequest(String url, Map<String, String> params, Map<String, String> headers) {
        super(url, params, headers);
    }

    @Override
    protected RequestBody buildRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }



    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.put(requestBody).build();
    }
}
