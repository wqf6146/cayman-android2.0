package com.spark.szhb_master.activity.Treasure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.SpinnerActivity;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.BitmapUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.ShareUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.WonderfulStringUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/26 0026.
 */
public class ShowTreaActivity extends BaseActivity{

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.iamge_shang)
    ImageView iamge_shang;
    @BindView(R.id.image_frd)
    ImageView image_frd;@BindView(R.id.tvSave)
    ImageView tvSave;@BindView(R.id.wx_frd)
    ImageView wx_frd;@BindView(R.id.wx_cal)
    ImageView wx_cal;
    @BindView(R.id.rela_frd)
    LinearLayout rela_frd;
    private String url_wx;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(ShowTreaActivity.this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.show_frd_layout;
    }
    @Override
    protected void initData() {
        super.initData();
        tvGoto.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setBackground(null);
        tvTitle.setText(getResources().getString(R.string.invitation_code));
        llTitle.setBackgroundDrawable(getResources().getDrawable(R.mipmap.trea_back_head));


        Intent intent = getIntent();
        String url = intent.getStringExtra("img");
        if (!WonderfulStringUtils.isEmpty(url))
            Glide.with(this).load(url).into(iamge_shang);
    }

    private Bitmap saveBitmap;
    private User user;
    @Override
    protected void initView() {
        super.initView();
        user = MyApplication.getApp().getCurrentUser();
        image_frd.post(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isEmpty(user.getPromotionPrefix(), user.getPromotionCode()))
                    return;
                saveBitmap = createQRCode(user.getPromotionPrefix() + user.getPromotionCode()+"&from=treasure", Math.min(image_frd.getWidth(), image_frd.getHeight()));
                image_frd.setImageBitmap(saveBitmap);
            }
        });
        url_wx = user.getPromotionPrefix() + user.getPromotionCode()+"&from=treasure";
    }

    @Override
    protected void setListener() {
        super.setListener();
    }

    @OnClick({R.id.ivBack,R.id.tvSave,R.id.wx_cal,R.id.wx_frd})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.wx_cal:
                wx_cal.setEnabled(false);
                ShareUtils.shareWeb(ShowTreaActivity.this, url_wx, GlobalConstant.share_title +"--"+ GlobalConstant.share_des, GlobalConstant.share_description,
                        GlobalConstant.logo_trea, R.mipmap.frd_wx, SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.wx_frd:
                wx_frd.setEnabled(false);
                ShareUtils.shareWeb(ShowTreaActivity.this, url_wx,  GlobalConstant.share_title, GlobalConstant.share_description,
                        GlobalConstant.logo_trea, R.mipmap.frd_wx, SHARE_MEDIA.WEIXIN);
                break;
            case R.id.tvSave:
                if (!PermissionUtils.isCanUseStorage(activity))
                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                else try {
                    save(getBitmap(rela_frd));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        wx_cal.setEnabled(true);
        wx_frd.setEnabled(true);
    }

    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(ShowTreaActivity.this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

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
            tvSave.setVisibility(View.VISIBLE);
            wx_cal.setVisibility(View.VISIBLE);
            wx_frd.setVisibility(View.VISIBLE);
            ToastUtils.showToast(getString(R.string.savesuccess));
        }
    }

    /**
     * @param view 需要截取图片的view
     * @return 截图
     */
    private Bitmap getBitmap(View view) throws Exception {
        tvSave.setVisibility(View.GONE);
        wx_cal.setVisibility(View.GONE);
        wx_frd.setVisibility(View.GONE);
        if (view == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-view.getScrollX(), -view.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        view.draw(canvas);// 将 view 画到画布上
        return screenshot;
    }


    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    try {
                        save(getBitmap(rela_frd));
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
}
