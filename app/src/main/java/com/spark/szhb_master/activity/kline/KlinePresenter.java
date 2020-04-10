package com.spark.szhb_master.activity.kline;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/1/17.
 */

public class KlinePresenter implements KlineContract.Presenter {
    private DataSource dataRepository;
    private KlineContract.View view;

    public KlinePresenter(DataSource dataRepository, KlineContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void KData(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getKDataUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    view.KDataSuccess(new JSONArray(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.dpPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.dpPostFail(code, toastMessage);
            }
        });
    }

    @Override
    public void allCurrency() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getAllCurrency(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    List<Currency> objs = new Gson().fromJson(response, new TypeToken<List<Currency>>() {
                    }.getType());
                    view.allCurrencySuccess(objs);
                } catch (Exception ex) {
                    view.dpPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.dpPostFail(code, toastMessage);
            }
        });
    }

    @Override
    public void doCollect(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getAddUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.doDeleteOrCollectSuccess(object.optString("message"));
                    } else {
                        view.dpPostFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.dpPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.dpPostFail(code, toastMessage);
            }
        });
    }

    @Override
    public void doDelete(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getDeleteUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.doDeleteOrCollectSuccess(object.optString("message"));
                    } else {
                        view.dpPostFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.dpPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.dpPostFail(code, toastMessage);
            }
        });
    }
}
