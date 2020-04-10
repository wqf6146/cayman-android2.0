package com.spark.szhb_master.utils.okhttp;


public interface LoadDataCallback {
    void onDataLoaded(String response, int requstCode);

    void onDataNotAvailable(Integer code, String toastMessage);

}
