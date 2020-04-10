package com.spark.szhb_master.activity.aboutus;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.AppInfo;
import com.spark.szhb_master.entity.Vision;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class AboutUsPresenter implements AboutUsContract.Presenter {
    private final DataSource dataRepository;
    private final AboutUsContract.View view;

    public AboutUsPresenter(DataSource dataRepository, AboutUsContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void appInfo() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getAppInfoUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        AppInfo appInfo = new Gson().fromJson(object.getJSONObject("data").toString(), AppInfo.class);
                        view.appInfoSuccess(appInfo);
                    } else {
                        view.appInfoFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.appInfoFail(JSON_ERROR, null);
                }
                view.appInfoSuccess((AppInfo) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.appInfoFail(code, toastMessage);
            }
        });
    }



    @Override
    public void getNewVision() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getNewVision(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        Vision vision = new Gson().fromJson(object.toString(), new TypeToken<Vision>() {
                        }.getType());
                        view.getNewVersionSuccess(vision);
                    } else {
                        view.getNewVersionFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.getNewVersionFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getNewVersionFail(JSON_ERROR, null);
            }
        });
    }


}
