package com.spark.szhb_master.activity.main.presenter;

import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/2/25.
 */

public class CommonPresenter {
    private CollectView view;
    private DataSource dataRepository;

    public CommonPresenter(DataSource dataRepository, CollectView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    public void delete(HashMap<String, String> params, final int position) {
        dataRepository.doStringPost(UrlFactory.getDeleteUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.deleteSuccess(object.optString("message"), position);
                    } else {
                        view.deleteFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.deleteFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.deleteFail(code, toastMessage);
            }
        });
    }

    public void add(HashMap<String, String> params, final int position) {
        dataRepository.doStringPost(UrlFactory.getAddUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.addSuccess(object.optString("message"), position);
                    } else {
                        view.addFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.addFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.addFail(code, toastMessage);
            }
        });
    }
}
