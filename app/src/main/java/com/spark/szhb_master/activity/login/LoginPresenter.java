package com.spark.szhb_master.activity.login;


import com.google.gson.Gson;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private final DataSource dataRepository;
    private final LoginContract.View view;

    public LoginPresenter(DataSource dataRepository, LoginContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void login(HashMap<String, String> params) {
        view.displayLoadingPopup();

        dataRepository.doStringPostJson(UrlFactory.getLoginUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                try {
                    String response = (String) obj;
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        User user = new Gson().fromJson(object.getJSONObject("data").toString(), User.class);
                        view.loginSuccess(user);
                    } else {
                        view.loginFail(object.getInt("code"), object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.loginFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.loginFail(code, toastMessage);
            }
        });
    }

    @Override
    public void captch() {
        view.displayLoadingPopup();
//        dataRepository.doStringPost(UrlFactory.getCaptchaUrl(), new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                view.hideLoadingPopup();
//                String response = (String) obj;
//                try {
//                    JSONObject object = new JSONObject(response);
//                    view.captchSuccess(object);
//                } catch (Exception e) {
//                    view.captchFail(JSON_ERROR, null);
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.hideLoadingPopup();
//                view.captchFail(code, toastMessage);
//            }
//        });
    }

    @Override
    public void googleLogin(HashMap<String, String> params) {
        view.displayLoadingPopup();
//        dataRepository.doStringPost(UrlFactory.googleLoginUrl(), params, new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                view.hideLoadingPopup();
//                String response = (String) obj;
//                JSONObject object = null;
//                try {
//                    object = new JSONObject(response);
//                    int code = object.optInt("code");
//                    if (code == 0) {
//                        User user = new Gson().fromJson(object.getJSONObject("data").toString(), User.class);
//                        view.googleLoginSuccess(user);
//                    } else {
//                        view.googleLoginFail(object.optInt("code"), object.optString("message"));
//                    }
//                } catch (Exception e) {
//                    view.googleLoginFail(JSON_ERROR, null);
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.hideLoadingPopup();
//                view.googleLoginFail(code, toastMessage);
//            }
//        });
    }
}
