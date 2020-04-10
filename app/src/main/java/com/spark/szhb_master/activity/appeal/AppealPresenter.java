package com.spark.szhb_master.activity.appeal;


import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class AppealPresenter implements AppealContract.Presenter {
    private final DataSource dataRepository;
    private final AppealContract.View view;

    public AppealPresenter(DataSource dataRepository, AppealContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void appeal(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getAppealUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.appealSuccess(object.getString("message"));
                    } else {
                        view.appealFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.appealFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.appealFail(code, toastMessage);
            }
        });
    }

    @Override
    public void uploadBase64Pic(HashMap<String, String> params, final int type) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getUploadPicUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.uploadBase64PicSuccess(object.optString("data"), type);
                    } else {
                        view.appealFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.appealFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.appealFail(code, toastMessage);

            }
        });
    }

    @Override
    public void uploadImageFile(File file, final int type) {
        view.displayLoadingPopup();
        dataRepository.doUploadFile(UrlFactory.getUploadPicFileUrl(), file, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.uploadBase64PicSuccess(object.optString("data"), type);
                    } else {
                        view.appealFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.appealFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.appealFail(code, toastMessage);
            }
        });
    }

}
