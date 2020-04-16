package com.spark.szhb_master.serivce;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.factory.socket.JWebSocketClient;
import com.spark.szhb_master.factory.socket.NEWCMD;
import com.spark.szhb_master.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.WebSocket;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;


import java.net.URI;

/**
 * author: wuzongjie
 * time  : 2018/4/27 0027 10:40
 * desc  : 行情的Socket Service
 * 基本可以实现socket 的发送接受数据，socket 心跳包的重连机制
 * 但是Service没有包活
 */

public class MyTextService extends Service {

    private static final String ip_port = "ws://39.98.150.252:8080/ws";//行情 真实
    private JWebSocketClient mWebSocketClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetMessage(SocketMessage message) {
        if (message.getCode() == 0) { // 行情方面的推送
            LogUtils.i("MyTextService 接受的信息" + message.getCmd());
            sendMsg(message.getBody());
        }
    }

    /**
     * 初始化websocket连接
     */
    private void initSocketClient() {
        URI uri = URI.create(ip_port);
        mWebSocketClient = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                Log.e("JWebSocketClientService", "收到的消息：" + message);

                try{
                    if (!TextUtils.isEmpty(message) && !message.equals("pong")){
                        JSONObject res = new JSONObject(message);

                        String ch = res.getString("ch");
                        NEWCMD cmd = NEWCMD.fromTypeName(ch);
                        String symbol = null;
                        if (cmd == null){
                            if (ch.indexOf("trade.detail") != -1){
                                cmd = NEWCMD.SUBSCRIBE_SYMBOL_DETAIL;
                            }else if (ch.indexOf("depth.step") != -1){
                                cmd = NEWCMD.SUBSCRIBE_SYMBOL_DEPTH;
                                try {
                                    symbol = ch.split("_")[0].split("\\.")[1];
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }else if (ch.indexOf("klist") != -1){
                                cmd = NEWCMD.SUBSCRIBE_SYMBOL_KLIST;
                                try {
                                    symbol = ch.split("_")[0].split("\\.")[1];
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }

                        EventBus.getDefault().post(new SocketResponse(cmd, res.getString("data")).setRemark(symbol));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                Log.e("JWebSocketClientService", "websocket连接成功");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.e("JWebSocketClient", "onClose()");
            }

            @Override
            public void onError(Exception ex) {
                Log.e("JWebSocketClient", "onError()");
            }
        };
        connect();
    }

    /**
     * 连接websocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
//                    if (mWebSocketClient!=null)
//                        mWebSocketClient.connectBlocking();
                    if (mWebSocketClient == null) {
                        return;
                    }
                    if (!mWebSocketClient.isOpen()) {
                        if (mWebSocketClient.getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
                            mWebSocketClient.connect();
                        } else if (mWebSocketClient.getReadyState().equals(ReadyState.CLOSING) || mWebSocketClient.getReadyState().equals(ReadyState.CLOSED)) {
                            mWebSocketClient.reconnect();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        initSocketClient();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeConnect();
        EventBus.getDefault().unregister(this);
    }

    private void sendMsg(String msg) {
        if (null != mWebSocketClient && !TextUtils.isEmpty(msg)) {
            if (mWebSocketClient.isOpen()){
                Log.e("JWebSocketClientService", "发送的消息：" + msg);
                mWebSocketClient.send(msg);
            }
        }
    }

    /**
     * 断开连接
     */
    private void closeConnect() {
        try {
            if (null != mWebSocketClient) {
                mWebSocketClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWebSocketClient = null;
        }
    }

    private static final long HEART_BEAT_RATE = 20 * 1000;//每隔20秒进行一次对长连接的心跳检测
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable  = new Runnable() {
        @Override
        public void run() {
            Log.e("JWebSocketClientService", "心跳包检测websocket连接状态");
            if (mWebSocketClient != null) {
                if (mWebSocketClient.isClosed()) {
                    reconnectWs();
                }
            } else {
                //如果client已为空，重新初始化连接
                mWebSocketClient = null;
                initSocketClient();
            }
            //每隔一定的时间，对长连接进行一次心跳检测
            sendMsg("ping");
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("JWebSocketClientService", "开启重连");
                    mWebSocketClient.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
