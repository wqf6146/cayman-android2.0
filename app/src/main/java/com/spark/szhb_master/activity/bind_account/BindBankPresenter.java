package com.spark.szhb_master.activity.bind_account;

import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class BindBankPresenter implements BindAccountContact.BankPresenter {
    private final DataSource dataRepository;
    private final BindAccountContact.BankView view;

    public BindBankPresenter(DataSource dataRepository, BindAccountContact.BankView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void doBindBank(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getBindBankUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.doBindBankSuccess(object.optString("message"));
                    } else {
                        view.doPostFailFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFailFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFailFail(code, toastMessage);
            }
        });
    }

    @Override
    public void doUpdateBank(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getUpdateBankUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.doUpdateBankSuccess(object.optString("message"));
                    } else {
                        view.doPostFailFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFailFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFailFail(code, toastMessage);
            }
        });
    }

}
