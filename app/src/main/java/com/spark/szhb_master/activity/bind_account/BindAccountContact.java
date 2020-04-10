package com.spark.szhb_master.activity.bind_account;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.AccountSetting;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface BindAccountContact {
    interface View extends Contract.BaseView<Presenter> {

        void getAccountSettingSuccess(AccountSetting obj);

        void getAccountSettingFail(Integer code, String toastMessage);

        void setAliStatusSuccess(String obj);

        void setAliStatusFail(String obj);

        void setBankStatusSuccess(String obj);

        void setBankStatusFail(String obj);

        void setWxStatusSuccess(String obj);

        void setWxStatusFail(String obj);

    }

    interface Presenter extends Contract.BasePresenter {
        void getAccountSetting();

        void setAliStatus(HashMap<String, String> params);

        void setBankStatus(HashMap<String, String> params);

        void setWxStatus(HashMap<String, String> params);

    }

    interface AliView extends Contract.BaseView<AliPresenter> {

        void doBindAliOrWechatSuccess(String obj);

        void doUpdateAliOrWechatSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);

        void uploadBase64PicSuccess(String obj);

    }

    interface AliPresenter extends Contract.BasePresenter {

        void uploadBase64Pic(HashMap<String, String> params);

        void getBindAli(HashMap<String, String> params);

        void getUpdateAli(HashMap<String, String> params);

        void getBindWeChat(HashMap<String, String> params);

        void getUpdateWeChat(HashMap<String, String> params);

        void uploadImageFile(File file);

    }

    interface BankView extends Contract.BaseView<BankPresenter> {

        void doBindBankSuccess(String obj);

        void doUpdateBankSuccess(String obj);

        void doPostFailFail(Integer code, String toastMessage);

    }

    interface BankPresenter extends Contract.BasePresenter {

        void doBindBank(HashMap<String, String> params);

        void doUpdateBank(HashMap<String, String> params);

    }
}
