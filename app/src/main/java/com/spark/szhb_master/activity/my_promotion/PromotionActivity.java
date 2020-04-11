package com.spark.szhb_master.activity.my_promotion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Treasure.ShowTreaActivity;
import com.spark.szhb_master.activity.Treasure.SuanActivity;
import com.spark.szhb_master.activity.message.WebViewActivity;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.entity.Promotion;
import com.spark.szhb_master.entity.PromotionRecord;
import com.spark.szhb_master.entity.PromotionReward;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.BitmapUtils;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.ShareUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 我的推广
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionActivity extends BaseActivity implements PromotionRewardContract.View, PromotionRecordFragment.OperateCallback, PromotionRewardFragment.OperateCallback {
    @BindView(R.id.ivShare)
    ImageView ibShare;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vpPager)
    ViewPager vpPager;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;
    @BindView(R.id.line_money)
    LinearLayout line_money;
    @BindView(R.id.line_num)
    LinearLayout line_num;
    @BindView(R.id.tvGoto)
    TextView tvGoto;
    @BindView(R.id.type_promot_o)
    TextView type_promot_o;
    @BindView(R.id.money_promot_o)
    TextView money_promot_o;
    @BindView(R.id.type_promot_t)
    TextView type_promot_t;
    @BindView(R.id.money_promot_t)
    TextView money_promot_t;
    @BindView(R.id.type_promot_th)
    TextView type_promot_th;
    @BindView(R.id.moeny_promot_th)
    TextView moeny_promot_th;
    @BindView(R.id.type_promot_fo)
    TextView type_promot_fo;
    @BindView(R.id.moeny_promot_fo)
    TextView moeny_promot_fo;
    @BindView(R.id.text_code)
    TextView text_code;
    @BindView(R.id.imgSave)
    ImageView imgSave;
    @BindView(R.id.imgCopy)
    ImageView imgCopy;
    @BindView(R.id.pro_rule)
    ImageView pro_rule;
    @BindView(R.id.wx_frd)
    ImageView wx_frd;
    @BindView(R.id.wx_cal)
    ImageView wx_cal;
    @BindView(R.id.line_three_text)
    LinearLayout line_three_text;


    @BindView(R.id.num_promot)
    TextView num_promot;
    private List<String> tabs = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private PopupWindow popWnd;
    private Bitmap saveBitmap;
    private LinearLayout llPromotion;
    private ImageView pupClose;
    private PromotionRewardContract.Presenter presenter;
    private User user;
    private String url_wx;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_promotion;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        ibShare.setVisibility(View.INVISIBLE);
        tvGoto.setVisibility(View.GONE);
        user = MyApplication.getApp().getCurrentUser();
        text_code.setText(user.getPromotionCode());
        url_wx = user.getPromotionPrefix() + user.getPromotionCode();
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.my_promotion));
        initTab();
        new PromotionRewardPresenter(Injection.provideTasksRepository(getApplicationContext()), PromotionActivity.this);
    }

    @Override
    public void setPresenter(PromotionRewardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void loadData() {
        super.loadData();
        presenter.getPromotion();
        presenter.getPromotionReward();
    }

    /**
     * 初始化tab栏
     */
    private void initTab() {
        tabs.add(getString(R.string.promote_friends));
        tabs.add(getString(R.string.my_commission));
        fragments.add(new PromotionRecordFragment());
        fragments.add(new PromotionRewardFragment());
        vpPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments, tabs));
        tab.setupWithViewPager(vpPager);
    }

    @OnClick({R.id.imgSave, R.id.imgCopy,R.id.pro_rule,R.id.wx_cal,R.id.wx_frd})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.pro_rule:
                Bundle bundle = new Bundle();
                bundle.putString("title", "活动规则");
                bundle.putString("url", GlobalConstant.pro_url);
                showActivity(WebViewActivity.class, bundle);
                break;
            case R.id.imgCopy:
                CommonUtils.copyText(activity, user.getPromotionPrefix() + user.getPromotionCode());
                ToastUtils.showToast(R.string.copy_success);
                break;
            case R.id.wx_cal:
                wx_cal.setEnabled(false);
                ShareUtils.shareWeb(PromotionActivity.this, url_wx, GlobalConstant.share_descriptionPro, GlobalConstant.share_description,
                        GlobalConstant.logo_frd, R.mipmap.frd_wx, SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.wx_frd:
                wx_frd.setEnabled(false);
                ShareUtils.shareWeb(PromotionActivity.this, url_wx,  GlobalConstant.share_title_pro, GlobalConstant.share_descriptionPro,
                        GlobalConstant.logo_frd, R.mipmap.frd_wx, SHARE_MEDIA.WEIXIN);
                break;
            case R.id.imgSave:
                showActivity(PromSaveActivity.class,null);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        wx_cal.setEnabled(true);
        wx_frd.setEnabled(true);
    }


    void showPop() {
        View contentView = LayoutInflater.from(PromotionActivity.this).inflate(R.layout.view_pop_promotion_share, null);
        popWnd = new PopupWindow(PromotionActivity.this);
        popWnd.setContentView(contentView);
        popWnd.setWidth((int) (MyApplication.getApp().getmWidth()));
//        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setHeight((int) (MyApplication.getApp().getmHeight() * 0.9));
        popWnd.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWnd.setOutsideTouchable(true);
        popWnd.setTouchable(true);
        popWnd.setFocusable(true);
        darkenBackground(0.4f);
        llPromotion = contentView.findViewById(R.id.llPromotion);
        TextView tvTitle = contentView.findViewById(R.id.tvTitle);
        final TextView tvLink = contentView.findViewById(R.id.tvLink);
        TextView tvCopy = contentView.findViewById(R.id.tvCopy);
        TextView tvSave = contentView.findViewById(R.id.tvSave);
        pupClose = contentView.findViewById(R.id.pupClose);
        final ImageView ivPromotion = contentView.findViewById(R.id.ivPromotion);

        ivPromotion.post(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isEmpty(user.getPromotionPrefix(), user.getPromotionCode()))
                    return;
                saveBitmap = createQRCode(user.getPromotionPrefix() + user.getPromotionCode(), Math.min(ivPromotion.getWidth(), ivPromotion.getHeight()));
                ivPromotion.setImageBitmap(saveBitmap);
            }
        });
        tvTitle.setText(user.getNick_name() + getString(R.string.reward_invita));
        tvLink.setText(user.getPromotionPrefix() + user.getPromotionCode());
        View rootview = LayoutInflater.from(PromotionActivity.this).inflate(R.layout.activity_promotion, null);

        pupClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWnd.dismiss();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PermissionUtils.isCanUseStorage(activity))
                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                else try {
                    save(getBitmap(llPromotion));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.copyText(activity, user.getPromotionPrefix() + user.getPromotionCode());
                ToastUtils.showToast(R.string.copy_success);
            }
        });

        popWnd.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
    }

    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(PromotionActivity.this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    try {
                        save(getBitmap(llPromotion));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    public static Bitmap createQRCode(String text, int size) {
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 2);   //设置白边大小 取值为 0- 4 越大白边越大
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = 0xff000000;
                    } else {
                        pixels[y * size + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void save(Bitmap saveBitmap) {
        File file = BitmapUtils.save(saveBitmap);
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            pupClose.setVisibility(View.VISIBLE);
            ToastUtils.showToast(getString(R.string.savesuccess));
        }
    }

    /**
     * @param view 需要截取图片的view
     * @return 截图
     */
    private Bitmap getBitmap(View view) throws Exception {
        if (view == null) {
            return null;
        }
        pupClose.setVisibility(View.GONE);
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-view.getScrollX(), -view.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        view.draw(canvas);// 将 view 画到画布上
        return screenshot;
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


    private String wcnmd;
    private int totalElements;


    @Override
    public void getPromotionRewardSuccess(List<PromotionReward> objs, Promotion obj, String wcnm) {
//        num_promot.setText(total);
        wcnmd = wcnm;
    }

    @Override
    public void getPromotionSuccess(List<PromotionRecord> obj) {
        totalElements = (obj.size());
        if (is == 0) {
            num_promot.setText(totalElements + " "+getResources().getString(R.string.people));
        }
    }

    @Override
    public void getPromotionFail(Integer code, String toastMessage) {

    }


    @Override
    public void getPromotionRewardFail(Integer code, String toastMessage) {

    }


    private int is = 0;

    @Override
    public void select() {
        line_money.setVisibility(View.VISIBLE);
        line_num.setVisibility(View.GONE);
        is = 1;

        if (StringUtils.isEmpty(wcnmd)) return;
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(wcnmd, type);
        if (is == 1) {
            int count = 0;
            String key ;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Log.v("TAG", entry.getKey() + "|" + entry.getValue());
                if (entry.getKey().equals("GalaxyChain")){
                    key = "GCC";
                }else {
                    key = entry.getKey();
                }
                if (count == 0) {
                    line_three_text.setVisibility(View.GONE);
                    type_promot_o.setText(key);
                    money_promot_o.setText(entry.getValue());
                }
                if (count == 1) {
                    type_promot_t.setText(key);
                    money_promot_t.setText(entry.getValue());
                }
                if (count == 2) {
                    line_three_text.setVisibility(View.VISIBLE);
                    type_promot_th.setText(key);
                    moeny_promot_th.setText(entry.getValue());
                }
                if (count == 3) {
                    line_three_text.setVisibility(View.VISIBLE);
                    type_promot_fo.setText(key);
                    moeny_promot_fo.setText(entry.getValue());
                }
                count++;

            }
        }
    }

    @Override
    public void selectReward() {
        line_num.setVisibility(View.VISIBLE);
        line_money.setVisibility(View.GONE);
        is = 0;
    }
}
