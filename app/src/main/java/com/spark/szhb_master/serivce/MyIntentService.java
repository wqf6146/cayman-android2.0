package com.spark.szhb_master.serivce;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2018/8/2 0002.
 */

public class MyIntentService extends IntentService {
    public static final String ACTION_INIT_APP_CREATE = "com.spark.digiccy.serivce.action";
    private static Context mContext;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(name);
    }

    /**
     * 启动调用
     *
     * @param context
     */
    public static void start(Context context) {
        mContext = context;
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_INIT_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_APP_CREATE.equals(action)) {
            }
        }
    }
}
