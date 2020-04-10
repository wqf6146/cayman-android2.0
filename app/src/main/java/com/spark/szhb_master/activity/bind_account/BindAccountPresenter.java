package com.spark.szhb_master.activity.bind_account;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.AccountSetting;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class BindAccountPresenter implements BindAccountContact.Presenter {
    private final DataSource dataRepository;
    private final BindAccountContact.View view;

    public BindAccountPresenter(DataSource dataRepository, BindAccountContact.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getAccountSetting() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getAccountSettingUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        AccountSetting objs = new Gson().fromJson(object.getJSONObject("data").toString(), new TypeToken<AccountSetting>() {
                        }.getType());
                        view.getAccountSettingSuccess(objs);
                    } else {
                        view.getAccountSettingFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.getAccountSettingFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getAccountSettingFail(code, toastMessage);
            }
        });
    }

    @Override
    public void setAliStatus(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.setAliStatusUrl(),params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.setAliStatusSuccess(object.optString("message"));
                    } else {
                        view.setAliStatusFail(object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.getAccountSettingFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getAccountSettingFail(code, toastMessage);
            }
        });
    }

    @Override
    public void setBankStatus(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.setBankStatusUrl(),params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.setBankStatusSuccess(object.optString("message"));
                    } else {
                        view.setBankStatusFail(object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.getAccountSettingFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getAccountSettingFail(code, toastMessage);
            }
        });
    }

    @Override
    public void setWxStatus(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.setWxStatusUrl(),params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.setWxStatusSuccess(object.optString("message"));
                    } else {
                        view.setWxStatusFail(object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.getAccountSettingFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getAccountSettingFail(code, toastMessage);
            }
        });
    }

}
