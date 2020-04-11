package com.spark.szhb_master.activity.order;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.OrderDetial;
import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.factory.socket.SocketFactory;
import com.spark.szhb_master.utils.CopyToast;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;
import okhttp3.Response;


/**
 * Created by Administrator on 2018/8/27 0027.
 */

public class OrderPayWayActivity extends BaseActivity implements OrderDetailContract.View, ISocket.TCPCallback {


    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.text_name)
    TextView text_name;
    @BindView(R.id.name_payway_wx)
    TextView name_payway_wx;
    @BindView(R.id.bank_payway_wx)
    TextView bank_payway_wx;
    @BindView(R.id.wx_name)
    TextView wx_name;
    @BindView(R.id.liner_radio_wx)
    LinearLayout liner_radio_wx;
    @BindView(R.id.liner_radio)
    LinearLayout liner_radio;
    @BindView(R.id.name_payway)
    TextView name_payway;
    @BindView(R.id.bank_payway)
    TextView bank_payway;
    @BindView(R.id.bankt_payway)
    TextView bankt_payway;
    @BindView(R.id.num_payway)
    TextView num_payway;
    @BindView(R.id.order_payway)
    TextView order_payway;
    @BindView(R.id.money_payway)
    TextView money_payway;
    @BindView(R.id.image_name)
    ImageView image_name;
    @BindView(R.id.image_bank)
    ImageView image_bank;
    @BindView(R.id.image_bankt)
    ImageView image_bankt;
    @BindView(R.id.image_num)
    ImageView image_num;
    @BindView(R.id.image_order)
    ImageView image_order;
    @BindView(R.id.image_name_wx)
    ImageView image_name_wx;
    @BindView(R.id.image_bank_wx)
    ImageView image_bank_wx;
    @BindView(R.id.image_bankt_wx)
    ImageView image_bankt_wx;
    @BindView(R.id.img_switch)
    ImageView img_switch;
    @BindView(R.id.time_other)
    TextView time_other;
    @BindView(R.id.time_yhk)
    TextView time_yhk;
    private OrderDetailContract.Presenter presenter;
    private OrderDetial orderDetial;
    private String orderSn;
    private int payway;
    private int i = 0;
    private long time;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_order_pay;
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        orderSn = bundle.getString("orderSn");
        payway = bundle.getInt("payway");
        time = bundle.getLong("time");
        new OrderDetailPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);


    }


    @Override
    protected void loadData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("orderSn", orderSn);
//        presenter.payDone(map);
        presenter.orderDetail(map);

    }

    private void setViews() {
        if (orderDetial.getType() == 0){
            setTitle(getString(R.string.text_buy)+orderDetial.getUnit());
        }
        name_payway_wx.setText(orderDetial.getPayInfo().getRealName());
        money_payway.setText(orderDetial.getMoney() + " CNY" );

        if (payway == 1) {
            bank_payway_wx.setText(orderDetial.getPayInfo().getWechatPay().getWechatNo());
        } else if (payway == 2) {
            bank_payway_wx.setText(orderDetial.getPayInfo().getAlipay().getAliNo());

        } else if (payway == 0) {
            name_payway.setText(orderDetial.getPayInfo().getRealName());
            bank_payway.setText(orderDetial.getPayInfo().getBankInfo().getBank());
            order_payway.setText(orderDetial.getOrderSn());
            bankt_payway.setText(orderDetial.getPayInfo().getBankInfo().getBranch());
            num_payway.setText(orderDetial.getPayInfo().getBankInfo().getCardNo());


        }

        fillCodeView(time);
        switchLiner();
    }


//    presenter.payDone(map);


    @OnClick({R.id.tvConfirm, R.id.image_name, R.id.image_bank, R.id.image_bankt, R.id.image_num, R.id.image_order, R.id.img_switch, R.id.image_name_wx, R.id.image_bank_wx, R.id.image_bankt_wx})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String toast = "  复制成功  ";
        switch (v.getId()) {
            case R.id.tvConfirm:
                HashMap<String, String> map = new HashMap<>();
                map.put("orderSn", orderSn);
                presenter.payDone(map);
                break;
            case R.id.image_name:
                // 将文本内容放到系统剪贴板里。
                cm.setText(name_payway.getText());
                CopyToast.showText(this, toast);
                break;
            case R.id.image_bank:
                cm.setText(bank_payway.getText());
                CopyToast.showText(this, toast);
                break;
            case R.id.image_bankt:
                cm.setText(bankt_payway.getText());
                CopyToast.showText(this, toast);
                break;
            case R.id.image_num:
                cm.setText(num_payway.getText());
                CopyToast.showText(this, toast);
                break;
            case R.id.image_order:
                cm.setText(order_payway.getText());
                CopyToast.showText(this, toast);
                break;
            case R.id.image_name_wx:
                cm.setText(name_payway_wx.getText());
                CopyToast.showText(this, toast);
                break;
            case R.id.image_bank_wx:
                cm.setText(bank_payway_wx.getText());
                CopyToast.showText(this, toast);
                break;
            case R.id.image_bankt_wx:
                if (payway == 1){
                    if (orderDetial.getPayInfo().getWechatPay().getQrCodeUrl() != null) {
                        showPopWindow(orderDetial.getPayInfo().getWechatPay().getQrCodeUrl());
                    }
                }else if (payway == 2){
                    if (orderDetial.getPayInfo().getAlipay().getQrCodeUrl() != null) {
                        showPopWindow(orderDetial.getPayInfo().getAlipay().getQrCodeUrl());
                    }
                }
                break;
            case R.id.img_switch:
                // 将文本内容放到系统剪贴板里。
                switchimg();
                break;

        }

    }

    private void switchimg() {
        if (i == 0) {
            i = 1;
            img_switch.setImageResource(R.drawable.shape_sxy);
            tvConfirm.setBackgroundResource(R.color.btn_hover);
            tvConfirm.setTextColor(getResources().getColor(R.color.white));
            tvConfirm.setEnabled(true);
        } else if (i == 1) {
            i = 0;
            img_switch.setImageResource(R.drawable.shape_kxy);
            tvConfirm.setBackgroundResource(R.color.uneable);
            tvConfirm.setTextColor(getResources().getColor(R.color.uneable_text));
            tvConfirm.setEnabled(false);
        }
    }

    private void switchLiner() {
        if (payway == 0) {
            liner_radio_wx.setVisibility(View.GONE);
            liner_radio.setVisibility(View.VISIBLE);
        } else if (payway == 1) {
            liner_radio_wx.setVisibility(View.VISIBLE);
            liner_radio.setVisibility(View.GONE);
            text_name.setText(getString(R.string.weichat));
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_wx);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            text_name.setCompoundDrawables(drawable, null, null, null);
            wx_name.setText("微信账号");

        } else if (payway == 2) {
            liner_radio_wx.setVisibility(View.VISIBLE);
            liner_radio.setVisibility(View.GONE);
            text_name.setText(getString(R.string.ali));
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_zfb);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            text_name.setCompoundDrawables(drawable, null, null, null);
            wx_name.setText("支付宝账号");
        }

    }

    @Override
    public void setPresenter(OrderDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void orderDetailSuccess(OrderDetial obj) {
        if (obj != null) {
            orderDetial = obj;
            setViews();
        }
    }

    @Override
    public void payDoneSuccess(String obj) {
        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        showActivity(MyOrderActivity.class,null);
        finish();
        JSONObject json = buildBodyJson();
        SocketFactory.produceSocket(ISocket.C2C).sendRequest(ISocket.CMD.SEND_CHAT, json.toString().getBytes(), OrderPayWayActivity.this);
    }

    @Override
    public void cancleSuccess(String obj) {

    }

    @Override
    public void releaseSuccess(String obj) {

    }

    @Override
    public void downloadSuccess(Response response) {
        InputStream inputStream = response.body().byteStream(); // 将响应数据转化为输入流数据
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = simpleDate.format(now.getTime());
        File folderFile = new File(dir);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        File file = new File(dir + fileName + ".jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            android.os.Message message = android.os.Message.obtain();
            message.what = 2;
            handler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {

    }

    private JSONObject buildBodyJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("orderId", orderDetial.getOrderSn());
            //obj.put("uid", orderDetial.getMyId());
            obj.put("uidFrom", MyApplication.getApp().getCurrentUser().getId());
            obj.put("uidTo", orderDetial.getHisId());
            obj.put("nameTo", orderDetial.getOtherSide());
            obj.put("nameFrom", MyApplication.getApp().getCurrentUser().getNick_name());
            obj.put("messageType", 1);
            obj.put("content", "");
            obj.put("avatar", MyApplication.getApp().getCurrentUser().getAvatar());
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void dataSuccess(ISocket.CMD cmd, String response) {

    }

    @Override
    public void dataFail(int code, ISocket.CMD cmd, String errorInfo) {

    }

    private String downloadUrl;
    private String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cayman/";//图片/
    private PopupWindow popWnd;

    public void showPopWindow(String url) {
        downloadUrl = url;
        View contentView = LayoutInflater.from(OrderPayWayActivity.this).inflate(R.layout.view_pop_order_detail_code, null);
        popWnd = new PopupWindow(OrderPayWayActivity.this);
        popWnd.setContentView(contentView);
        popWnd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWnd.setOutsideTouchable(true);
        popWnd.setTouchable(true);
        darkenBackground(0.4f);
        ImageView ivQR = contentView.findViewById(R.id.ivQR);
        Glide.with(this).load(url).centerCrop().into(ivQR);
        ivQR.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!PermissionUtils.isCanUseStorage(OrderPayWayActivity.this))
                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                else {
                    ToastUtils.showToast(getString(R.string.download_tag));
                    presenter.doDownload(downloadUrl);
                }
                return true;
            }
        });
        View rootview = LayoutInflater.from(OrderPayWayActivity.this).inflate(R.layout.activity_order_detial, null);
        popWnd.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
    }

    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

    }

    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(OrderPayWayActivity.this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    presenter.doDownload(downloadUrl);
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    ToastUtils.showToast(getString(R.string.storage_permission));
                    break;
            }
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(OrderPayWayActivity.this, getString(R.string.download_failed), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    popWnd.dismiss();
                    Toast.makeText(OrderPayWayActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    private CountDownTimer timer;
    private void fillCodeView(long time) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat formatter = new SimpleDateFormat("mm分ss秒");
                formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                String hms = formatter.format(millisUntilFinished);
                if (!StringUtils.isEmpty(hms)) {
                    if (payway == 0){
                        time_yhk.setText(getString(R.string.please) + hms + getString(R.string.payway_time));
                    }else {
                        time_other.setText(getString(R.string.please) + hms + getString(R.string.payway_time));
                    }
                }
            }

            @Override
            public void onFinish() {
                if (payway == 0){
                    time_yhk.setText("00:00:00");
                }else {
                    time_other.setText("00:00:00");
                }
                timer.cancel();
                timer = null;
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
