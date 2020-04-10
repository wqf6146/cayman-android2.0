package com.spark.szhb_master.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.widget.Toast;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2018/10/24 0024.
 */

public class AppRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(GlobalConstant.APP_ID);
    }
}
