package com.spark.szhb_master.activity.main;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.BannerEntity;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.entity.Coin;
import com.spark.szhb_master.entity.CoinInfo;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.entity.Favorite;
import com.spark.szhb_master.entity.Message;
import com.spark.szhb_master.entity.Plate;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.entity.Vision;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/2/24.
 */

public interface MainContract {
    interface View extends Contract.BaseView<Presenter> {

        void allCurrencySuccess(Object obj);

        void allCurrencyFail(Integer code, String toastMessage);

        void homeCurrencySuccess(String obj);

        void homeCurrencyFail(Integer code, String toastMessage);

        void findSuccess(List<Favorite> obj);

        void findFail(Integer code, String toastMessage);

        void getNewVersionSuccess(Vision obj);

        void getNewVersionFail(Integer code, String toastMessage);

        void getRateSuccess(double obj);

        void getRateFail(Integer code, String toastMessage);

    }

    interface Presenter extends Contract.BasePresenter {

        void allCurrency();

        void homeCurrency();

        void find();

        void getNewVersion();

        void getRate();

    }


    interface HomeView extends Contract.BaseView<HomePresenter> {

        void bannersSuccess(List<BannerEntity> obj);

        void bannersFail(Integer code, String toastMessage);

        void getMarqueeSuccess(List<Message> messages);

        void getMarqueeFail(Integer code, String toastMessage);

        void safeSettingFiled(Integer code, String toastMessage);

        void safeSettingSuccess(SafeSetting obj);

    }

    interface HomePresenter extends Contract.BasePresenter {

        void banners(HashMap<String, String> map);

        void getMarqueeText(HashMap<String, String> map);

        void safeSetting();
    }


    interface HeyuePresenter extends Contract.BasePresenter {

    }

    interface HeyueView extends Contract.BaseView<HeyuePresenter> {

    }

    interface MarketPresenter extends Contract.BasePresenter {

    }

    interface MarketView extends Contract.BaseView<MarketPresenter> {

    }

    interface ThreePresenter extends Contract.BasePresenter {

    }

    interface ThreeView extends Contract.BaseView<ThreePresenter> {
    }

    interface C2CPresenter extends Contract.BasePresenter {

        void all();

        void safeSetting();
    }

    interface C2CView extends Contract.BaseView<C2CPresenter> {

        void allSuccess(List<CoinInfo> obj);

        void doPostFail(Integer code, String toastMessage);

        void safeSettingSuccess(SafeSetting obj);
    }

    interface MyPresenter extends Contract.BasePresenter {

        void myWallet();
        void getUserInfo();
        void getNewVision();
        void safeSetting();
    }

    interface MyView extends Contract.BaseView<MyPresenter> {

        void myWalletSuccess(List<Coin> obj);
        void getUserInfoSuccess(User user);
        void doPostFail(Integer code, String toastMessage);
        void getNewVisionSuccess(Vision obj);
        void safeSettingSuccess(SafeSetting obj);
    }

    interface BuyPresenter extends Contract.BasePresenter {

        void exChange(String token, String symbol, String price, String amount, String direction, String type);

        void walletBase(String token, String s);

        void walletOther(String token, String s);

        void plate(String symbol);
    }

    interface BuyView extends Contract.BaseView<BuyPresenter> {

        void exChangeSuccess(String obj);

        void exChangeFail(Integer code, String toastMessage);

        void walletBaseSuccess(Coin obj);

        void walletBaseFail(Integer code, String toastMessage);

        void walletOtherSuccess(Coin obj);

        void walletOtherFail(Integer code, String toastMessage);

        void plateSuccess(Plate obj);

        void plateFail(Integer code, String toastMessage);
    }

    interface SellPresenter extends Contract.BasePresenter {

        void exChange(String token, String symbol, String price, String amount, String direction, String type);

        void walletBase(String token, String baseCoin);

        void walletOther(String token, String otherCoin);

        void plate(String symbol);
    }

    interface SellView extends Contract.BaseView<SellPresenter> {

        void exChangeSuccess(String obj);

        void exChangeFail(Integer code, String toastMessage);

        void walletBaseSuccess(Coin obj);

        void walletBaseFail(Integer code, String toastMessage);

        void walletOtherSuccess(Coin obj);

        void walletOtherFail(Integer code, String toastMessage);

        void plateSuccess(Plate obj);

        void plateFail(Integer code, String toastMessage);
    }

    interface C2CListPresenter extends Contract.BasePresenter {

        void advertise(HashMap<String, String> params);

        void safeSetting(int position);
    }

    interface C2CListView extends Contract.BaseView<com.spark.szhb_master.activity.main.MainContract.C2CListPresenter> {

        void advertiseSuccess(C2C obj);

        void advertiseFail(Integer code, String toastMessage);

        void doPostFail(Integer code, String toastMessage);

        void safeSettingSuccess(SafeSetting obj,int position);
    }

    interface EntrustPresenter extends Contract.BasePresenter {

        void entrust(String token, int pageSize, int pageNo, String symbol);

    }

    interface EntrustView extends Contract.BaseView<EntrustPresenter> {

        void entrustSuccess(List<Entrust> obj);

        void entrustFail(Integer code, String toastMessage);

        void cancleEntrustSuccess(String obj);

        void cancleEntrustFail(Integer code, String toastMessage);
    }












}
