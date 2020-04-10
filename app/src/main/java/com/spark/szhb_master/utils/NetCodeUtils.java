package com.spark.szhb_master.utils;


import android.support.v4.app.Fragment;

import com.spark.szhb_master.R;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;

/**
 * Created by wonderful on 2017/5/23.
 * 网络请求错误
 */

public class NetCodeUtils {

    private static String unknown_error;
    private static String no_network;
    private static String parse_error;
    private static String login_invalid;
    private static String no_data;


    static {
        unknown_error = MyApplication.getApp().getResources().getString(R.string.request_error);
        no_network = MyApplication.getApp().getResources().getString(R.string.no_network);
        parse_error = MyApplication.getApp().getResources().getString(R.string.parse_error);
        login_invalid = MyApplication.getApp().getResources().getString(R.string.login_invalid);
        no_data = MyApplication.getApp().getResources().getString(R.string.no_data);
    }

    public static void checkedErrorCode(BaseActivity activity, Integer code, String toastMessage) {
        String toast = "";
        switch (code) {
            case GlobalConstant.NO_DATA:
                toast = no_data;
                ToastUtils.showToast(toast);
                return;
            case GlobalConstant.TOKEN_DISABLE1:
                toast = login_invalid;
                MyApplication.getApp().loginAgain(activity);
                ToastUtils.showToast(toast);
                return;
            case GlobalConstant.TOKEN_DISABLE2:
                toast = login_invalid;
                MyApplication.getApp().loginAgain(activity);
                ToastUtils.showToast(toast);
                return;
            case GlobalConstant.JSON_ERROR:
                toast = parse_error;
                ToastUtils.showToast(toast);
                return;
            case GlobalConstant.OKHTTP_ERROR:
                if (CommonUtils.isNetConnect()) toast = unknown_error;
//                if (CommonUtils.isNetConnect());
                else toast = no_network;
                ToastUtils.showToast(toast);
                return;
        }
        toast = toastMessage;
        if (!StringUtils.isEmpty(toastMessage)) {
            ToastUtils.showToast(toast);
            return;
        }
    }

    public static void checkedErrorCode(Fragment fragment, Integer code, String toastMessage) {
        String toast = "";
        switch (code) {
            case GlobalConstant.NO_DATA:
                toast = no_data;
                ToastUtils.showToast(toast);
                return;
            case GlobalConstant.TOKEN_DISABLE1:
                toast = login_invalid;
                MyApplication.getApp().loginAgain(fragment);
                MyApplication.getApp().deleteCurrentUser();
                ToastUtils.showToast(toast);
                return;
            case GlobalConstant.TOKEN_DISABLE2:
                toast = login_invalid;
                MyApplication.getApp().loginAgain(fragment);
                ToastUtils.showToast(toast);
                return;
            case GlobalConstant.JSON_ERROR:
                toast = parse_error;
                ToastUtils.showToast(toast);
                return;
            case GlobalConstant.OKHTTP_ERROR:
                if (CommonUtils.isNetConnect()) toast = unknown_error;
                else toast = no_network;
                ToastUtils.showToast(toast);
                return;
        }
        toast = toastMessage;
        if (!StringUtils.isEmpty(toastMessage)) {
            ToastUtils.showToast(toast);
            return;
        }
    }

}
