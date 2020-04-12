package com.spark.szhb_master.data;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface DataSource {

    void doStringPost(String url, HashMap<String, String> params, DataCallback dataCallback);

    void doStringPost(String url, DataCallback dataCallback);

    void doStringPostJson(final String url, HashMap params,  DataCallback dataCallback);

    void doStringPostJson(final String url, DataCallback dataCallback);

    void doStringGet(String url, DataCallback dataCallback);
    void doStringGet(String url,HashMap<String, String> params, DataCallback dataCallback);

    void doStringPut(String url,HashMap<String, String> params, DataCallback dataCallback);

    void doDownload(String Url, DataCallback dataCallback);

    void doUploadFile(String url, File file, DataCallback dataCallback);

    void myMatch(String token,  DataCallback dataCallback);

    void myMatchHis(String token,String dataRange,int pageNo,int pageSize,int type,  DataCallback dataCallback);

    void getCheckMatch(String token,String symbol,DataCallback dataCallback);

    void getStartMatch(String token,String amount,String symbol,DataCallback dataCallback);

    interface DataCallback {

        void onDataLoaded(Object obj);

        void onDataNotAvailable(Integer code, String toastMessage);
    }
}
