package com.spark.szhb_master.activity.country;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.Country;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/3/1.
 */

public class CountryPresenter implements CountryContract.Presenter {
    private final DataSource dataRepository;
    private final CountryContract.View view;

    public CountryPresenter(DataSource dataRepository, CountryContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void country() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getCountryUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Country> objs = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Country>>() {
                        }.getType());
                        view.countrySuccess(objs);
                    } else {
                        view.countryFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.countryFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.countryFail(code, toastMessage);

            }
        });
    }

}
