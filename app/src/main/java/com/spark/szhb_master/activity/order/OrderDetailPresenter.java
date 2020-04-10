package com.spark.szhb_master.activity.order;


import com.google.gson.Gson;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.OrderDetial;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Response;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class OrderDetailPresenter implements OrderDetailContract.Presenter {
    private final DataSource dataRepository;
    private final OrderDetailContract.View view;

    public OrderDetailPresenter(DataSource dataRepository, OrderDetailContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void orderDetail(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getOrderDetailUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        OrderDetial orderDetial = new Gson().fromJson(object.getJSONObject("data").toString(), OrderDetial.class);
                        view.orderDetailSuccess(orderDetial);
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
    public void payDone(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getpayDoneUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.payDoneSuccess(object.getString("message"));
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
    public void cancle(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getCancleUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.cancleSuccess(object.getString("message"));
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
    public void release(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getReleaseOrderUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.releaseSuccess(object.getString("message"));
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                }
                view.releaseSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(code, toastMessage);

            }
        });
    }

    @Override
    public void doDownload(String url) {
//        view.displayLoadingPopup();
        dataRepository.doDownload(url, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
//                view.hideLoadingPopup();
                view.downloadSuccess((Response) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.hideLoadingPopup();
                view.doPostFail(code, toastMessage);
            }
        });
    }

}
