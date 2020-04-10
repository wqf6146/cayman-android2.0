package com.spark.szhb_master.activity.safe;


import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GoogleBindPresenter implements GoogleContract.BindPresenter {
    private final DataSource dataRepository;
    private final GoogleContract.BindView view;

    public GoogleBindPresenter(DataSource dataRepository, GoogleContract.BindView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void doBind(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getBindGoogleCode(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 0) {
                        view.doBindSuccess(object.optString("message"));
                    } else {
                        view.doBindFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.doBindFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doBindFail(code, toastMessage);
            }
        });
    }

    @Override
    public void bindcode() {
        view.displayLoadingPopup();
        dataRepository.doStringGet(UrlFactory.bindgoogleCode(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 0) {
                        view.getbindcodeSuccess(object.optString("message"));
                    } else {
                        view.getbindcodeFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void modifyvcode() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.modifyvcode(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 0) {
                        view.modifyvcodeSuccess(object.optString("message"));
                    } else {
                        view.modifyvcodeFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void modifyvAlidate(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.modifyvAlidate(),params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 0) {
                        view.modifyvAlidateSuccess(object.optString("message"));
                    } else {
                        view.modifyvAlidateFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void modifyvBind(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.modifyvBind(),params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 0) {
                        view.modifyvBindSuccess(object.optString("message"));
                    } else {
                        view.modifyvBindFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }
}
