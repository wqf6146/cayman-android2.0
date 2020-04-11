package com.spark.szhb_master.data;


import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.entity.GccMatch;
import com.spark.szhb_master.entity.MatchHis;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.okhttp.StringCallback;
import com.spark.szhb_master.utils.okhttp.OkhttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;
import static com.spark.szhb_master.utils.GlobalConstant.OKHTTP_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */
public class RemoteDataSource implements DataSource {
    private static RemoteDataSource INSTANCE;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    private RemoteDataSource() {

    }

    /**
     * postJson请求
     *
     * @param url
     * @param params
     * @param dataCallback
     */
    @Override
    public void doStringPostJson(final String url, HashMap params, final DataCallback dataCallback) {
        String token = "";
        if (MyApplication.getApp().getCurrentUser() != null)
            token = MyApplication.getApp().getCurrentUser().getToken() == null ? "" : MyApplication.getApp().getCurrentUser().getToken();

        JSONObject jsonBody = new JSONObject(params);
        OkhttpUtils.postJson().url(url).addHeader("Content-Type","application/json").body(jsonBody.toString()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) { // 请求异常，服务器异常，解析异常
                e.printStackTrace();
                /*
                  请求服务异常，注释掉，否则交易频繁点击报错
                 */
//                dataCallback.onDataNotAvailable(GlobalConstant.OKHTTP_ERROR, null);

            }

            @Override
            public boolean onResponse(String response) {
                LogUtils.i("urll===" + url + ",,,,onResponse" + response);
                dataCallback.onDataLoaded(response);
                return false;
            }

        });
    }

    /**
     * postJson请求
     *
     * @param url
     * @param
     * @param dataCallback
     */
    @Override
    public void doStringPostJson(final String url, final DataCallback dataCallback) {
        String token = "";
        if (MyApplication.getApp().getCurrentUser() != null)
            token = MyApplication.getApp().getCurrentUser().getToken() == null ? "" : MyApplication.getApp().getCurrentUser().getToken();

        OkhttpUtils.postJson().url(url).addHeader("Authorization",token)
                .addHeader("Content-Type","application/json").body("").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) { // 请求异常，服务器异常，解析异常
                e.printStackTrace();
                /*
                  请求服务异常，注释掉，否则交易频繁点击报错
                 */
//                dataCallback.onDataNotAvailable(GlobalConstant.OKHTTP_ERROR, null);
            }

            @Override
            public boolean onResponse(String response) {
                LogUtils.i("urll===" + url + ",,,,onResponse" + response);
                dataCallback.onDataLoaded(response);
                return false;
            }

        });
    }

    /**
     * 有参数的post请求
     *
     * @param url
     * @param params
     * @param dataCallback
     */
    @Override
    public void doStringPost(final String url, HashMap<String, String> params, final DataCallback dataCallback) {
        String token = "";
        if (MyApplication.getApp().getCurrentUser() != null)
            token = MyApplication.getApp().getCurrentUser().getToken() == null ? "" : MyApplication.getApp().getCurrentUser().getToken();

        OkhttpUtils.post().url(url).addHeader("Authorization",token)
                .addHeader("Content-Type","application/json").addParams(params).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) { // 请求异常，服务器异常，解析异常
                e.printStackTrace();
                /*
                请求服务异常，注释掉，否则交易频繁点击报错
                 */
//                dataCallback.onDataNotAvailable(GlobalConstant.OKHTTP_ERROR, null);
            }

            @Override
            public boolean onResponse(String response) {
                LogUtils.i("urll===" + url + ",,,,onResponse" + response);
                dataCallback.onDataLoaded(response);
                return false;
            }

        });
        if (params != null) {
            LogUtils.i("head   x-auth-token===" + token);
            String strParams = "";
            for (String key : params.keySet()) {
                strParams = strParams + key + "=" + params.get(key) + "&";
            }
            LogUtils.i("传参==" + url + "?" + strParams);
        }
    }

    /**
     * 无参数的post请求
     *
     * @param url
     * @param dataCallback
     */
    @Override
    public void doStringPost(final String url, final DataCallback dataCallback) {
        String token = "";
        if (MyApplication.getApp().getCurrentUser() != null)
            token = MyApplication.getApp().getCurrentUser().getToken() == null ? "" : MyApplication.getApp().getCurrentUser().getToken();
        OkhttpUtils.post().url(url).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                dataCallback.onDataNotAvailable(GlobalConstant.OKHTTP_ERROR, null);
            }

            @Override
            public boolean onResponse(String response) {
                LogUtils.i("urll===" + url + ",,,,onResponse" + response);
                dataCallback.onDataLoaded(response);
                return false;
            }

        });
        LogUtils.i("传参==" + url);
    }

    /**
     * get请求
     *
     * @param params
     * @param dataCallback
     */
    @Override
    public void doStringGet(String params, final DataCallback dataCallback) {
        String token = "";
        if (MyApplication.getApp().getCurrentUser() != null)
            token = MyApplication.getApp().getCurrentUser().getToken() == null ? "" : MyApplication.getApp().getCurrentUser().getToken();
        LogUtils.i("token==" + token);
        OkhttpUtils.get().url(params).addHeader("Authorization", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public boolean onResponse(String response) {
                LogUtils.i("onResponse：" + response.toString());
                dataCallback.onDataLoaded(response);
                return false;
            }

        });
    }

    /**
     * 下载
     */
    @Override
    public void doDownload(String url, final DataCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onDataLoaded(response);
            }
        });
    }

    /**
     * 上传文件
     *
     * @param url
     * @param file
     */
    @Override
    public void doUploadFile(String url, File file, final DataCallback dataCallback) {
        String token = "";
        if (MyApplication.getApp().getCurrentUser() != null)
            token = MyApplication.getApp().getCurrentUser().getToken() == null ? "" : MyApplication.getApp().getCurrentUser().getToken();
        LogUtils.i("传参 url==" + url);
        LogUtils.i("传参 x-auth-token==" + token);
        RequestParams params = new RequestParams(url);
        params.addHeader("x-auth-token", token);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", file));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setConnectTimeout(60000);
        params.setReadTimeout(60000);
        params.setRequestBody(body);
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("result==" + result);
                dataCallback.onDataLoaded(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, cex.toString());
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void myMatch(String token, DataCallback dataCallback) {

    }

    @Override
    public void myMatchHis(String token,String dataRange,int pageNo,int pageSize,int type, final DataCallback dataCallback) {
        OkhttpUtils.post().url(UrlFactory.getMatchHisUrl()).addHeader("x-auth-token", token)
                .addParams("dataRange",  dataRange)
                .addParams("pageNo", pageNo+"")
                .addParams("pageSize", pageSize+"")
                .addParams("type", type+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public boolean onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (!response.equals("")) {
                        List<MatchHis> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<MatchHis>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);

                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
                return false;
            }
        });
    }

    @Override
    public void getCheckMatch(String token, String symbol, final DataCallback dataCallback) {
        OkhttpUtils.post().url(UrlFactory.getCheckMatchUrl())
                .addHeader("x-auth-token", token)
                .addParams("symbol",symbol)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public boolean onResponse(String response) {
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        GccMatch objs = gson.fromJson(object.toString(), new TypeToken<GccMatch>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
                return false;
            }
        });
    }

    @Override
    public void getStartMatch(String token, String amount, String symbol,final DataCallback dataCallback) {
        OkhttpUtils.post().url(UrlFactory.getStartMatchUrl())
                .addHeader("x-auth-token", token)
                .addParams("amount", amount)
                .addParams("symbol",symbol)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public boolean onResponse(String response) {
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
                return false;
            }
        });
    }
}
