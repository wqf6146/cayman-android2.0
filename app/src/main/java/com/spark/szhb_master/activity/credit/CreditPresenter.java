package com.spark.szhb_master.activity.credit;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.Credit;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class CreditPresenter implements CreditContract.Presenter {
    private final DataSource dataRepository;
    private final CreditContract.View view;

    public CreditPresenter(DataSource dataRepository, CreditContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void credit(HashMap params) {
        view.displayLoadingPopup();
        dataRepository.doStringPostJson(UrlFactory.getCreditUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 1) {
                        view.doCreditSuccess(object.optString("msg"));
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("msg"));
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
    public void getCreditInfo() {
//        view.displayLoadingPopup();
//        dataRepository.doStringPost(UrlFactory.getCreditInfo(), new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                view.hideLoadingPopup();
//                String response = (String) obj;
//                try {
//                    JSONObject object = new JSONObject(response);
//                    Credit.DataBean objs = new Gson().fromJson(object.getJSONObject("data").toString(), new TypeToken<Credit.DataBean>() {
//                    }.getType());
//                    view.getCreditInfoSuccess(objs);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    view.doPostFail(JSON_ERROR, null);
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.hideLoadingPopup();
//                view.doPostFail(code, toastMessage);
//
//            }
//        });
    }

    @Override
    public void uploadImageFile(File file, final int type) {
//        view.displayLoadingPopup();
//        dataRepository.doUploadFile(UrlFactory.getUploadPicFileUrl(), file, new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                view.hideLoadingPopup();
//                String response = (String) obj;
//                try {
//                    JSONObject object = new JSONObject(response);
//                    if (object.optInt("code") == 0) {
//                        view.uploadBase64PicSuccess(object.optString("data"));
//                    } else {
//                        view.doPostFail(object.optInt("code"), object.optString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    view.doPostFail(JSON_ERROR, null);
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.hideLoadingPopup();
//                view.doPostFail(code, toastMessage);
//            }
//        });
    }

}
