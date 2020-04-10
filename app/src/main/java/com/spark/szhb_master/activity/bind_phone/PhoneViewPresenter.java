package com.spark.szhb_master.activity.bind_phone;


import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class PhoneViewPresenter implements PhoneViewContract.Presenter {
    private final DataSource dataRepository;
    private final PhoneViewContract.View view;

    public PhoneViewPresenter(DataSource dataRepository, PhoneViewContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }



    @Override
    public void sendChangePhoneCode() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getSendChangePhoneCodeUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.sendChangePhoneCodeSuccess(object.getString("message"));
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(code, toastMessage);
            }
        });
    }

    @Override
    public void findChangePhoneCode(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getFindChangePhoneCodeUrl(),params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.findChangePhoneCodeSuccess(object.getString("message"));
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(code, toastMessage);
            }
        });
    }

}
