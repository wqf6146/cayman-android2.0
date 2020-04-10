package com.spark.szhb_master.utils;

import android.os.Handler;
import android.os.Looper;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2018/4/8.
 */
public class TCPUtils {
    private static TCPUtils wonderfulTCPUtils = null;
    private static final String lock = "lock";
    private static Socket socket = null;
    private static DataInputStream dis = null;
    private static DataOutputStream dos = null;
    private static SocketThread socketThread = null;

//    public static final String ip = "52.80.242.238";//行情
    public static final String ip = "47.74.180.137";//行情
    public static final int port = 28901;//行情

    private Handler handler;
    public static final long sequenceId = 10001;//以后用于token

    public static final int requestid = 0;//请求ID

    public static final int version = 1;

    public static final String terminal = "1001";  //安卓:1001,苹果:1002,WEB:1003,PC:1004

    public TCPUtils() {
        handler = new Handler(Looper.getMainLooper());
        startListenerThread();
    }

    public static synchronized TCPUtils getInstance() {
        return wonderfulTCPUtils == null ? wonderfulTCPUtils = new TCPUtils() : wonderfulTCPUtils;
    }

    public synchronized void startListenerThread() {
        if (socketThread == null || !socketThread.isAlive()) {
            socketThread = new SocketThread();
            socketThread.start();
        }
    }

    public static void releaseSocket() {
        if (socket != null) {
            try {
                dis.close();
                dos.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SocketThread extends Thread {

        private TCPCallback tcpCallback;

        @Override
        public void run() {
            synchronized (lock) {
                if (socket == null) try {
                    socket = new Socket(ip, port);
                    dis = new DataInputStream(socket.getInputStream());
                    dos = new DataOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            while (true) {
                try {
                    dealResponse(tcpCallback);
                } catch (IOException e) {
                    socketThread = null;
                    break;
                }
            }
        }

        public void setTcpCallback(TCPCallback tcpCallback) {
            this.tcpCallback = tcpCallback;
        }
    }

    public void sendRequest(final CMD cmd, final byte[] body, TCPCallback callback) {
        socketThread.setTcpCallback(callback);
        new Thread() {
            @Override
            public void run() {
                synchronized (lock) {
                    if (socket == null) try {
                        socket = new Socket(ip, port);
                        dis = new DataInputStream(socket.getInputStream());
                        dos = new DataOutputStream(socket.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                toRequest(cmd, body);
            }
        }.start();
    }

    private static void toRequest(CMD cmd, byte[] body) {
        try {
            byte[] requestBytes = buildRequest(cmd, body);
            dos.write(requestBytes);
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] buildRequest(CMD cmd, byte[] body) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            int length = body == null ? 26 : (26 + body.length);
            dos.writeInt(length);
            dos.writeLong(sequenceId);
            dos.writeShort(cmd.getCode());
            dos.writeInt(version);
            byte[] terminalBytes = terminal.getBytes();
            dos.write(terminalBytes);
            dos.writeInt(requestid);
            if (body != null) dos.write(body);
            return bos.toByteArray();
        } catch (IOException ex) {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void dealResponse(final TCPCallback tcpCallback) throws IOException {
        int length = dis.readInt();
        long sequenceId = dis.readLong();
        short code = dis.readShort();
        final int responseCode = dis.readInt();
        int requestId = dis.readInt();
        byte[] buffer = new byte[length - 22];
        final CMD cmd = CMD.findObjByCode(code);
        int nIdx = 0;
        int nReadLen = 0;
        while (nIdx < buffer.length) {
            nReadLen = dis.read(buffer, nIdx, buffer.length - nIdx);
            if (nReadLen > 0) {
                nIdx += nReadLen;
            } else {
                break;
            }
        }
        final String str = new String(buffer);
        LogUtils.i("cmd:" + code + ",length:" + length + ",sequenceId:" +
                sequenceId + ",responseCode:" + responseCode + ",sequenceId:" + requestId + ",str:" + str);
//
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tcpCallback == null) return;
                if (responseCode == 200) {
                    tcpCallback.dataSuccess(cmd, str);
                } else {
                    tcpCallback.dataFail(responseCode, cmd, str);
                }
            }
        });
//        if (cmd.isUnSubscrible()) {
//            synchronized (this) {
//                try {
//                    wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    /**
     * 增加枚举也需要增加对应的 取消Code数组
     */
    public enum CMD {
        COMMANDS_VERSION((short) 1),
        SUBSCRIBE_SYMBOL_THUMB((short) 20001), UNSUBSCRIBE_SYMBOL_THUMB((short) 20002),
        PUSH_SYMBOL_THUMB((short) 20003),
        SUBSCRIBE_SYMBOL_KLINE((short) 20011), UNSUBSCRIBE_SYMBOL_KLINE((short) 20012),
        PUSH_SYMBOL_KLINE((short) 20013),
        SUBSCRIBE_EXCHANGE_TRADE((short) 20021), UNSUBSCRIBE_EXCHANGE_TRADE((short) 20022),
        PUSH_EXCHANGE_TRADE((short) 20023),
        SUBSCRIBE_CHAT((short) 20031), UNSUBSCRIBE_CHAT((short) 20032),
        PUSH_CHAT((short) 20033),
        SEND_CHAT((short) 20034),
        SUBSCRIBE_GROUP_CHAT((short) 20035);

        int[] waitCodes = new int[]{20002, 20012, 20022, 20032, 20031};

        private short code;

        CMD(short code) {
            this.code = code;
        }

        public short getCode() {
            return code;
        }

        public static CMD findObjByCode(short code) {
            for (CMD cmd : CMD.values()) {
                if (cmd.getCode() == code) return cmd;
            }
            return null;
        }

        public boolean isUnSubscrible() {
            for (int i = 0; i < waitCodes.length; i++) {
                if (code == waitCodes[i]) return true;
            }
            return false;
        }

    }

    public interface TCPCallback {
        void dataSuccess(CMD cmd, String response);

        void dataFail(int code, CMD cmd, String errorInfo);

    }

}
