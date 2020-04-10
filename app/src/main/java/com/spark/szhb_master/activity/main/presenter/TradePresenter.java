package com.spark.szhb_master.activity.main.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.main.TradeContract;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.entity.Exchange;
import com.spark.szhb_master.entity.Money;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.SymbolInfo;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;
import static com.spark.szhb_master.utils.GlobalConstant.OKHTTP_ERROR;

/**
 * author: wuzongjie
 * time  : 2018/4/17 0017 19:14
 * desc  :
 */

public class TradePresenter implements TradeContract.Presenter {
    private TradeContract.View view;
    private DataSource dataRepository;

    public TradePresenter(DataSource dataRepository, TradeContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    /**
     * 获取盘口信息
     *
     * @param params
     */
    @Override
    public void getExchange(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getPlateUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JsonObject object = new JsonParser().parse(response).getAsJsonObject();
                    JsonArray ask = object.getAsJsonArray("ask").getAsJsonArray();
                    List<Exchange> askList = new Gson().fromJson(ask, new TypeToken<List<Exchange>>() {
                    }.getType());
                    JsonArray bid = object.getAsJsonArray("bid").getAsJsonArray();
                    List<Exchange> bidList = new Gson().fromJson(bid, new TypeToken<List<Exchange>>() {
                    }.getType());
                    view.getExchangeSuccess(askList, bidList);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (StringUtils.isNotEmpty(response)) { // 请求出错且有返回数据的状况，用此种方法解析
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = MyApplication.getApp().getResources().getString(R.string.str_get_exchange_info);
                            view.doPostFail(jsonObject.optInt("code"), msg + jsonObject.optInt("code") + "：" + jsonObject.optString("message"));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        view.doPostFail(JSON_ERROR, null);
                    }
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

    @Override
    public void getCurrentEntrust(HashMap<String, String> params) {
//        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getEntrustUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                view.hideLoadingPopup();
                try {
                    JSONObject object = new JSONObject(response);
                    List<Entrust> objs = new Gson().fromJson(object.getJSONArray("content").toString(), new TypeToken<List<Entrust>>() {
                    }.getType());
                    view.getCurrentEntrustSuccess(objs);
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

    /**
     * 提交委托
     *
     * @param params
     */
    @Override
    public void commitEntrust(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getExChangeUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.commitEntrustSuccess(object.optString("message"));
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
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

    @Override
    public void cancelEntrust(String orderId) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getCancleEntrustUrl() + orderId, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.cancelSuccess(object.optString("message"));
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
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

    @Override
    public void getWallet(final int type, String url) {
//        view.displayLoadingPopup();
        dataRepository.doStringPost(url, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    Money money = new Gson().fromJson(response, Money.class);
                    view.getWalletSuccess(money, type);
                } catch (Exception e) {
                    if (StringUtils.isNotEmpty(response)) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            view.doPostFail(jsonObject.optInt("code"), jsonObject.optString("message"));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    view.doPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

    @Override
    public void getSymbolInfo(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getSymbolInfo(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                if (response == null || TextUtils.isEmpty(response))
                    return;
                SymbolInfo info = new Gson().fromJson(response, SymbolInfo.class);
                view.getSymbolInfoResult(info.getCoinScale(), info.getBaseCoinScale());
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getSymbolInfoResult(1, 1);
            }
        });
    }

    @Override
    public void addCollect(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getAddUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.addCollectSuccess(object.optString("message"));
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(OKHTTP_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

    @Override
    public void deleteCollect(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getDeleteUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.deleteCollectSuccess(object.optString("message"));
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(OKHTTP_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(OKHTTP_ERROR, null);
            }
        });
    }

    @Override
    public void safeSetting() {
        dataRepository.doStringPost(UrlFactory.getSafeSettingUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        SafeSetting safeSetting = new Gson().fromJson(object.getJSONObject("data").toString(), SafeSetting.class);
                        view.safeSettingSuccess(safeSetting);
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
                view.doPostFail(JSON_ERROR, null);
            }
        });
    }
}
