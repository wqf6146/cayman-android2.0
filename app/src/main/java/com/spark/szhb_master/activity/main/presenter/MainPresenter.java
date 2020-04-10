package com.spark.szhb_master.activity.main.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.activity.main.MainContract;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.entity.Favorite;
import com.spark.szhb_master.entity.Vision;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/2/25.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private DataSource dataRepository;

    public MainPresenter(DataSource dataRepository, MainContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void allCurrency() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getAllCurrency(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    List<Currency> currencies = new Gson().fromJson(response, new TypeToken<List<Currency>>() {
                    }.getType());
                    view.allCurrencySuccess(currencies);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (StringUtils.isNotEmpty(response)) { // 请求出错且有返回数据的状况，用此种方法解析
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            view.allCurrencyFail(jsonObject.optInt("code"), jsonObject.optInt("code") + "：" + jsonObject.optString("message"));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        view.allCurrencyFail(JSON_ERROR, null);
                    }

                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.allCurrencyFail(JSON_ERROR, null);
            }
        });
    }

    @Override
    public void homeCurrency() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getAllCurrencys(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                view.homeCurrencySuccess(response);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.homeCurrencyFail(JSON_ERROR, null);
            }
        });
    }

    @Override
    public void find() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getFindUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    List<Favorite> objs = new Gson().fromJson(response, new TypeToken<List<Favorite>>() {
                    }.getType());
                    view.findSuccess(objs);
                } catch (Exception e) {
                    if (StringUtils.isNotEmpty(response)) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            view.findFail(jsonObject.optInt("code"), jsonObject.optString("message"));
                        } catch (JSONException e1) {
                            view.findFail(JSON_ERROR, null);
                            e1.printStackTrace();
                        }
                    } else {
                        e.printStackTrace();
                        view.findFail(JSON_ERROR, null);
                    }
                }

            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.findFail(JSON_ERROR, null);
            }
        });
    }

    @Override
    public void getNewVersion() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getNewVision(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        Vision vision = new Gson().fromJson(object.toString(), new TypeToken<Vision>() {
                        }.getType());
                        view.getNewVersionSuccess(vision);
                    } else {
                        view.getNewVersionFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.getNewVersionFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getNewVersionFail(JSON_ERROR, null);
            }
        });
    }

    @Override
    public void getRate() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getRateUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JsonObject object = new JsonParser().parse(response).getAsJsonObject();
                double rate = object.getAsJsonPrimitive("data").getAsDouble();
                view.getRateSuccess(rate);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getRateFail(JSON_ERROR, null);
            }
        });
    }

}
