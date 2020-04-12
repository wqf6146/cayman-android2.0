package com.spark.szhb_master.activity.signup;


import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private final DataSource dataRepository;
    private final SignUpContract.View view;

    public SignUpPresenter(DataSource dataRepository, SignUpContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getCode(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getPhoneCodeUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        //view.codeSuccess(object.optString("message"));
                    } else {
                        //view.codeFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //view.codeFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                //view.codeFail(code, toastMessage);
            }
        });
    }

    @Override
    public void sighUp(String url, HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPostJson(url, params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        view.sighUpSuccess(object.optString("msg"));
                    } else {
                        view.sighUpFail(object.getInt("code"), object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.sighUpFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.sighUpFail(code, toastMessage);
            }
        });
    }

    @Override
    public void captch() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getCaptchaUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    //view.captchSuccess((JSONObject) object);
                } catch (Exception e) {
                    //view.captchFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                //view.captchFail(code, toastMessage);
            }
        });
    }

}
