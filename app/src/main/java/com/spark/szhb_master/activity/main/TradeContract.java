package com.spark.szhb_master.activity.main;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.entity.Exchange;
import com.spark.szhb_master.entity.Money;
import com.spark.szhb_master.entity.SafeSetting;

import java.util.HashMap;
import java.util.List;

/**
 * author: wuzongjie
 * time  : 2018/4/17 0017 19:12
 * desc  :
 */

public interface TradeContract {
    interface View extends Contract.BaseView<Presenter> {
        void doPostFail(int e, String meg);

        void getExchangeSuccess(List<Exchange> ask, List<Exchange> bid);

        void getCurrentEntrustSuccess(List<Entrust> entrusts);

        void commitEntrustSuccess(String message);

        void cancelSuccess(String message);

        void getWalletSuccess(Money coin, int type);

        void getSymbolInfoResult(int one, int two);

        void addCollectSuccess(String message);

        void deleteCollectSuccess(String message);

        void safeSettingSuccess(SafeSetting obj);
    }

    interface Presenter {
        void getExchange(HashMap<String, String> params); // 获取盘口的信息

        void getCurrentEntrust(HashMap<String, String> params); // 获取当前的委托

        void commitEntrust(HashMap<String, String> params); // 提交委托

        void cancelEntrust(String orderId); // 取消委托

        void getWallet(int type, String url); // 获取钱包

        void getSymbolInfo(HashMap<String, String> params); // 获取精确度

        void addCollect(HashMap<String, String> params); // 添加收藏

        void deleteCollect(HashMap<String, String> params); // 删除收藏

        void safeSetting();
    }
}
