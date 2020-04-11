package com.spark.szhb_master.activity.my_promotion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Treasure.SuanActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.BitmapUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * Created by Administrator on 2018/10/31 0031.
 */
public class PromSaveActivity extends BaseActivity{


    @BindView(R.id.img_erwm)
    ImageView img_erwm;
    @BindView(R.id.line_btn)
    LinearLayout line_btn;@BindView(R.id.line_save)
    LinearLayout line_save; @BindView(R.id.text_head)
    TextView text_head; @BindView(R.id.text_name)
    TextView text_name;

    private User user;
    private Bitmap saveBitmap;
    @Override
    protected int getActivityLayoutId() {
        return R.layout.pro_save_layout;
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle("邀请海报");
        tvGoto.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        tvGoto.setVisibility(View.GONE);
        user = MyApplication.getApp().getCurrentUser();

        img_erwm.post(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isEmpty(user.getPromotionPrefix(), user.getPromotionCode()))
                    return;
                saveBitmap = createQRCode(user.getPromotionPrefix() + user.getPromotionCode(), Math.min(img_erwm.getWidth(), img_erwm.getHeight()));
                img_erwm.setImageBitmap(saveBitmap);
            }
        });
        if (!StringUtils.isEmpty(user.getNick_name())) {
            String str = user.getNick_name().substring(0, 1).toUpperCase();
            text_head.setText(str);
            text_name.setText(user.getNick_name()+"邀请您一起来Cayman");
        }

    }


    @OnClick({R.id.line_btn})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()){
            case R.id.line_btn:
                if (!PermissionUtils.isCanUseStorage(activity))
                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                else try {
                    save(getBitmap(line_save));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(PromSaveActivity.this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
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
            line_btn.setVisibility(View.VISIBLE);
            ToastUtils.showToast(getString(R.string.savesuccess));
        }
    }

    /**
     * @param view 需要截取图片的view
     * @return 截图
     */
    private Bitmap getBitmap(View view) throws Exception {

        line_btn.setVisibility(View.GONE);
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
                        save(getBitmap(line_save));
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
