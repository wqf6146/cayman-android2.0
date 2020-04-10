package com.spark.szhb_master.activity.main.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.Preheatinginfo;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/9/17 0017.
 */

public class DiggingPresenter implements TreasureContract.DiggingPresenter {

    private final DataSource dataRepository;
    private final TreasureContract.DiggingView view;

    public DiggingPresenter(DataSource dataRepository, TreasureContract.DiggingView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        view.setPresenter(this);
    }

    @Override
    public void getUserList() {
        dataRepository.doStringPost(UrlFactory.getactivityList(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Preheatinginfo> preheatinginfos = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Preheatinginfo>>() {
                        }.getType());
                        view.getUserListSuccess(preheatinginfos);
                    } else {
                        view.doFiled(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doFiled(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doFiled(code, toastMessage);
            }
        });
    }

    @Override
    public void getCheckJoin(HashMap<String, String> map) {
        dataRepository.doStringPost(UrlFactory.getCheckJoin(),map, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        int min = object.getJSONObject("data").getInt("minAmount");
                        int max = object.getJSONObject("data").getInt("availableAmount");
                        int rate = object.getJSONObject("data").getInt("rate");
                        view.getCheckJoinSuccess(min,max,rate);
                    } else {
                        view.doFiled(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doFiled(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doFiled(code, toastMessage);
            }
        });
    }

    @Override
    public void getJoin(HashMap<String, String> map) {
        dataRepository.doStringPost(UrlFactory.getJoin(),map, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {

                        view.getJoinSuccess(obj);
                    } else {
                        view.doFiled(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doFiled(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doFiled(code, toastMessage);
            }
        });
    }

    @Override
    public void safeSetting() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getSafeSettingUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        SafeSetting safeSetting = new Gson().fromJson(object.getJSONObject("data").toString(), SafeSetting.class);
                        view.safeSettingSuccess(safeSetting);
                    } else {
                        view.doFiled(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doFiled(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doFiled(JSON_ERROR, null);
            }
        });
    }
}
