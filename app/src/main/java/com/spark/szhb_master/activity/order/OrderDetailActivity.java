package com.spark.szhb_master.activity.order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.appeal.AppealActivity;
import com.spark.szhb_master.activity.chat.ChatActivity;
import com.spark.szhb_master.dialog.OrderCancleDialog;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.OrderDetial;
import com.spark.szhb_master.factory.socket.ISocket;
import com.spark.szhb_master.factory.socket.SocketFactory;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.DateUtils;
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

public class OrderDetailActivity extends BaseActivity implements OrderDetailContract.View, ISocket.TCPCallback {
    @BindView(R.id.tvOrderSn)
    TextView tvOrderSn;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvZhifubao)
    TextView tvZhifubao;
    @BindView(R.id.tvWeChat)
    TextView tvWeChat;
    @BindView(R.id.tvBankNo)
    TextView tvBankNo;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvPayDone)
    TextView tvPayDone;
    @BindView(R.id.tvPayAppeal)
    TextView tvPayAppeal;
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.tvAppeal)
    TextView tvAppeal;
    @BindView(R.id.llPayInfo)
    LinearLayout llPayInfo;
    @BindView(R.id.liner_radio)
    LinearLayout liner_radio;
    @BindView(R.id.llAppeal)
    LinearLayout llAppeal;
    @BindView(R.id.tvRelease)
    TextView tvRelease;
    @BindView(R.id.llOperate)
    LinearLayout llOperate;
    @BindView(R.id.ivZhifubaoQR)
    ImageView ivZhifubaoQR;
    @BindView(R.id.ivWeChatQR)
    ImageView ivWeChatQR;
    @BindView(R.id.tvOtherSide)
    TextView tvOtherSide;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvLastTime)
    TextView tvLastTime;
    @BindView(R.id.llLastTime)
    LinearLayout llLastTime;
    @BindView(R.id.tvBranch)
    TextView tvBranch;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.Rd_yhk)
    RadioButton Rd_yhk;
    @BindView(R.id.rbwx)
    RadioButton rbwx;
    @BindView(R.id.radio_group)
    RadioGroup radio_group;
    @BindView(R.id.rbzfb)
    RadioButton rbzfb;
    @BindView(R.id.rvMessageTag)
    RelativeLayout rvMessageTag;
    private String orderSn;
    private OrderDetailContract.Presenter presenter;
    private OrderDetial orderDetial;
    private String downloadUrl;
    private CountDownTimer timer;
    private OrderCancleDialog dialog;

    private String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cayman/";//图片/
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(OrderDetailActivity.this, getString(R.string.download_failed), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    popWnd.dismiss();
                    Toast.makeText(OrderDetailActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });
    private PopupWindow popWnd;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_order_detial;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
//        tvGoto.setVisibility(View.VISIBLE);
//        tvGoto.setText(getString(R.string.contact_with_me));
        rvMessageTag.setVisibility(View.VISIBLE);
        setTitle(getString(R.string.orderdetail));
        new OrderDetailPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderSn = bundle.getString("orderSn");
            boolean isChatList = bundle.getBoolean("isChatList");
            if (isChatList)
                tvGoto.setVisibility(View.GONE);
        }

//        Rd_yhk.setChecked(true);
        dialog = new OrderCancleDialog(this);
    }

    @Override
    protected void loadData() {
        getOrdetDetail();
    }


    @Override
    public void setPresenter(OrderDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @OnClick({R.id.ivMessage,R.id.tvPayDone, R.id.tvCancle, R.id.tvRelease, R.id.tvAppeal,R.id.tvPayAppeal, R.id.tvGoto,R.id.rbwx})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (orderDetial == null) {
            ToastUtils.showToast(getString(R.string.order_details_failed));
            getOrdetDetail();
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.tvPayDone:
//                showCofirmDialog(0);
                int id = radio_group.getCheckedRadioButtonId();
                int payway = 0;// yhk ,wx ,zfb,
                if (id == R.id.Rd_yhk){
                    payway = 0;
                }else if (id == R.id.rbwx){
                    payway = 1;
                }else if (id == R.id.rbzfb){
                    payway = 2;
                }else {
                    ToastUtils.showToast("请选择付款方式");
                    return;
                }
                bundle.putString("orderSn", orderSn);
                bundle.putInt("payway", payway);
                if (!StringUtils.isEmpty(hms) && !hms.equals("00分01秒")) {
                    bundle.putLong("time", time_millis);
                }else{
                    ToastUtils.show("付款时间已过",Toast.LENGTH_SHORT);
                    return;
                }
                showActivity(OrderPayWayActivity.class, bundle);
                finish();
                break;
            case R.id.tvCancle:
//                showCofirmDialog(1);
                dialog.show();

                break;
            case R.id.tvRelease:
                showReleaseDialog();
                break;
            case R.id.tvAppeal:
                bundle.putString("orderSn", orderDetial.getOrderSn());
                showActivity(AppealActivity.class, bundle);
                break;
            case R.id.tvPayAppeal:
                bundle.putString("orderSn", orderDetial.getOrderSn());
                showActivity(AppealActivity.class, bundle);
                finish();
                break;
            case R.id.ivMessage:
                bundle.putSerializable("orderDetial", orderDetial);
                showActivity(ChatActivity.class, bundle);
                break;
        }
    }


    /**
     * 订单放行
     */
    private void showReleaseDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_jypassword, null);
        TextView tvRelease = view.findViewById(R.id.tvRelease);
        final EditText etPassword = view.findViewById(R.id.etPassword);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        dialog.show();
        tvRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jyPassword = etPassword.getText().toString();
                if (StringUtils.isEmpty(jyPassword)) {
                    ToastUtils.showToast(getString(R.string.enter_money_pwd_tag));
                } else {
                    release(jyPassword);
                }
                dialog.dismiss();
            }
        });
    }


    /**
     * 对话框
     *
     * @param intType 0-付款完成，1-取消
     */
    private void showCofirmDialog(final int intType) {
        String content = "";
        if (intType == 0) {
            content = getString(R.string.confirm_pay_tag);
        } else {
            content = getString(R.string.confirm_go_on_canel_order_tag);

        }
        final MaterialDialog dialog = new MaterialDialog(activity);
        dialog.title(getString(R.string.warm_prompt)).titleTextColor(getResources().getColor(R.color.colorPrimary)).content(content).btnText(getString(R.string.cancle), getString(R.string.confirm)).setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("orderSn", orderSn);
                        if (intType == 0) {
                            presenter.payDone(map);
                        } else {
                            presenter.cancle(map);
                        }
                        dialog.superDismiss();
                    }
                });
        dialog.show();
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

    /**
     * 获取订单详情
     */
    private void getOrdetDetail() {
        HashMap<String, String> map = new HashMap<>();
        map.put("orderSn", orderSn);
        presenter.orderDetail(map);
    }

    /**
     * 订单放行
     *
     * @param jyPassword
     */
    private void release(String jyPassword) {
        HashMap<String, String> map = new HashMap<>();
        map.put("orderSn", orderSn);
        map.put("jyPassword", jyPassword);
        presenter.release(map);
    }


    @Override
    public void orderDetailSuccess(OrderDetial obj) {
        if (obj != null) {
            orderDetial = obj;
            setViews();
        }
    }

    private void setViews() {
        tvOtherSide.setText(orderDetial.getOtherSide());
        tvOrderSn.setText(orderDetial.getOrderSn());
        tvPrice.setText(orderDetial.getPrice() + " CNY");
        tvCount.setText(orderDetial.getAmount() + " " + orderDetial.getUnit());
        tvTotal.setText(orderDetial.getMoney() + " CNY");
        tvTime.setText(orderDetial.getCreateTime());
        if (orderDetial.getType() == 0) {
            final OrderDetial.PayInfoBean payInfoBean = orderDetial.getPayInfo();
            if (payInfoBean != null) {
                if (payInfoBean.getAlipay() == null) {
                    tvZhifubao.setText("");
                    ivZhifubaoQR.setVisibility(View.GONE);
                    rbzfb.setVisibility(View.GONE);
                } else {
                    tvZhifubao.setText(payInfoBean.getAlipay().getAliNo());
                    ivZhifubaoQR.setVisibility(View.VISIBLE);
                    ivZhifubaoQR.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (payInfoBean.getAlipay().getQrCodeUrl() != null) {
                                showPopWindow(payInfoBean.getAlipay().getQrCodeUrl());
                            } else
                                ToastUtils.showToast(getString(R.string.no_qrcode));
                        }
                    });
                }
                if (payInfoBean.getWechatPay() == null) {
                    tvWeChat.setText("");
                    ivWeChatQR.setVisibility(View.GONE);
                    rbwx.setVisibility(View.GONE);
                } else {
                    tvWeChat.setText(payInfoBean.getWechatPay().getWechatNo());
                    ivWeChatQR.setVisibility(View.VISIBLE);
                    ivWeChatQR.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (payInfoBean.getWechatPay().getQrCodeUrl() != null) {
                                showPopWindow(payInfoBean.getWechatPay().getQrCodeUrl());
                            } else
                                ToastUtils.showToast(getString(R.string.no_qrcode));
                        }
                    });
                }
                if (payInfoBean.getBankInfo() != null) {
                    tvBankNo.setText(payInfoBean.getBankInfo().getCardNo());
                    tvBranch.setText(payInfoBean.getBankInfo().getBank() + " " + payInfoBean.getBankInfo().getBranch());
                    tvName.setText(payInfoBean.getRealName());

                } else {
                    tvBranch.setVisibility(View.INVISIBLE);
                    tvName.setVisibility(View.INVISIBLE);
                    tvBankNo.setText("");
                    Rd_yhk.setVisibility(View.GONE);
                    if (payInfoBean.getWechatPay() != null) {
                        rbwx.setChecked(true);
                    }else{
                        rbzfb.setChecked(true);
                    }
                }
            }
        } else liner_radio.setVisibility(View.GONE);
//        } else llPayInfo.setVisibility(View.GONE);
        OrderFragment.Status status = OrderFragment.Status.values()[orderDetial.getStatus()];
        switch (status.getStatus()) {
            case 0:
                tvStatus.setText(getResources().getStringArray(R.array.order_status)[3]);
                break;
            case 1:
                if (orderDetial.getType() == 1){
                    tvStatus.setText("等待对方付款");
                }else {
                    tvStatus.setText(getResources().getStringArray(R.array.order_status)[0]);
                }
                break;
            case 2:
//                tvStatus.setText(getResources().getStringArray(R.array.order_status)[1]);
                tvStatus.setText(getResources().getString(R.string.waiting_currency));
                break;
            case 3:
                tvStatus.setText(getResources().getStringArray(R.array.order_status)[2]);
                break;
            case 4:
                tvStatus.setText(getResources().getStringArray(R.array.order_status)[4]);
                break;
        }
        if (orderDetial.getStatus() == 1 && orderDetial.getType() == 0){
            liner_radio.setVisibility(View.VISIBLE);
        }else{
            liner_radio.setVisibility(View.GONE);
            llLastTime.setVisibility(View.GONE);
            tvStatus.setGravity(Gravity.RIGHT);
        }
        int type = orderDetial.getType();
        showWhichViews(type, status);

        final HashMap<String, String> map = new HashMap<>();
        map.put("orderSn", orderSn);

        dialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getTvConfirm().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.cancle(map);
                dialog.dismiss();
            }
        });
    }

    public void showPopWindow(String url) {
        downloadUrl = url;
        View contentView = LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.view_pop_order_detail_code, null);
        popWnd = new PopupWindow(OrderDetailActivity.this);
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
                if (!PermissionUtils.isCanUseStorage(OrderDetailActivity.this))
                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                else {
                    ToastUtils.showToast(getString(R.string.download_tag));
                    presenter.doDownload(downloadUrl);
                }
                return true;
            }
        });
        View rootview = LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.activity_order_detial, null);
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
        AndPermission.with(OrderDetailActivity.this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
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

    private void showWhichViews(int type, OrderFragment.Status status) {
        switch (status) {
            case CANC:
                llOperate.setVisibility(View.GONE);
                llAppeal.setVisibility(View.GONE);
                break;
            case UNPAID:
                llAppeal.setVisibility(View.GONE);
                if (type == 0) {
                    llOperate.setVisibility(View.VISIBLE);
                    tvPayDone.setVisibility(View.VISIBLE);
                    tvCancle.setVisibility(View.VISIBLE);
                    llLastTime.setVisibility(View.VISIBLE);
                    getTime();
                    tvRelease.setVisibility(View.GONE);
                } else if (type == 1) {
                    llOperate.setVisibility(View.GONE);
                }
                break;
            case PAID:
                llOperate.setVisibility(View.VISIBLE);
                tvPayDone.setVisibility(View.GONE);
                tvPayAppeal.setVisibility(View.VISIBLE);
                llAppeal.setVisibility(View.GONE);
                if (type == 0) {
                    tvCancle.setVisibility(View.VISIBLE);
                    tvRelease.setVisibility(View.GONE);
                } else if (type == 1) {
                    tvCancle.setVisibility(View.GONE);
                    tvRelease.setVisibility(View.VISIBLE);
                }
                break;
            case DONE:
                llOperate.setVisibility(View.GONE);
                llAppeal.setVisibility(View.GONE);
                break;
            case COMPLAINING:
                llOperate.setVisibility(View.GONE);
                llAppeal.setVisibility(View.GONE);
                break;
        }
    }

    private void getTime() {
        long createTime = DateUtils.getTimeMillis("", orderDetial.getCreateTime());
        long timeLimit = (long) (orderDetial.getTimeLimit() * 60 * 1000);
        long currentTime = System.currentTimeMillis();
        if (createTime + timeLimit - currentTime > 0) {
            fillCodeView(createTime + timeLimit - currentTime);
        } else {
            tvStatus.setText("付款超时");
            tvLastTime.setText("00:00:00");
            tvLastTime.setVisibility(View.GONE);
        }
    }

    private long time_millis;
    private String hms;
    private void fillCodeView(final long time) {
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
                time_millis = millisUntilFinished;
                hms = formatter.format(millisUntilFinished);
                if (!StringUtils.isEmpty(hms)) {
                    tvLastTime.setText("剩"+formatter.format(millisUntilFinished));
                }
            }

            @Override
            public void onFinish() {
                tvLastTime.setText("00:00:00");
                tvStatus.setText("付款超时");
                tvLastTime.setVisibility(View.GONE);
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

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void payDoneSuccess(String obj) {
        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        finish();
        JSONObject json = buildBodyJson();
        SocketFactory.produceSocket(ISocket.C2C).sendRequest(ISocket.CMD.SEND_CHAT, json.toString().getBytes(), OrderDetailActivity.this);
    }

    @Override
    public void cancleSuccess(String obj) {
        ToastUtils.showToast(obj);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getOrdetDetail();
            }
        }, 500);
    }

    @Override
    public void releaseSuccess(String obj) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getOrdetDetail();
            }
        }, 500);
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
    public void dataSuccess(ISocket.CMD cmd, String response) {
    }

    @Override
    public void dataFail(int code, ISocket.CMD cmd, String errorInfo) {
    }
}
