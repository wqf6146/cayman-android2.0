package com.spark.szhb_master.activity.wallet_coin;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Coin;
import com.spark.szhb_master.utils.BitmapUtils;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充币
 */
public class RechargeActivity extends BaseActivity {
    @BindView(R.id.tvAddressText)
    TextView tvAddressText;
    @BindView(R.id.ivAddress)
    ImageView ivAddress;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.remake_address)
    TextView remake_address;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    @BindView(R.id.llAlbum)
    TextView llAlbum;
    @BindView(R.id.llCopy)
    TextView llCopy;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    private Coin coin;
    private Bitmap saveBitmap;
    @BindView(R.id.caymanaddress)
    LinearLayout caymanaddress; @BindView(R.id.caymanremake)
    LinearLayout caymanremake;
    @BindView(R.id.memberId)
    TextView memberId;
    @BindView(R.id.masteraddress)
    TextView masteraddress;
    @BindView(R.id.image_name_wx)
    TextView image_name_wx;@BindView(R.id.image_name)
    TextView image_name;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        final ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        image_name_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cm.setText(masteraddress.getText());
                ToastUtils.showToast("复制成功");
            }
        });
        image_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cm.setText(memberId.getText());
                ToastUtils.showToast("复制成功");
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        tvGoto.setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            coin = (Coin) bundle.getSerializable("coin");
            if (coin.getCoin().getUnit().equals("GCX")){
                caymanaddress.setVisibility(View.VISIBLE);
                caymanremake.setVisibility(View.VISIBLE);
                tvAddress.setVisibility(View.GONE);
                llCopy.setVisibility(View.GONE);
            }else{
                caymanaddress.setVisibility(View.GONE);
                caymanremake.setVisibility(View.GONE);
                tvAddress.setVisibility(View.VISIBLE);
                llCopy.setVisibility(View.VISIBLE);
            }
            if (coin != null) {
                tvTitle.setText(coin.getCoin().getUnit() + getString(R.string.charge_money));
                tvNotice.setText(getString(R.string.risk_warning) + coin.getCoin().getUnit() + getString(R.string.assets));
                tvAddressText.setText(coin.getCoin().getUnit() + " Address");
                tvAddress.setText(coin.getAddress());
            } else {
                tvAddress.setText(getString(R.string.no_address));
            }
            masteraddress.setText(coin.getCoin().getMasterAddress());
            memberId.setText(coin.getMemberId()+"");
        }
        ivAddress.post(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isEmpty(coin.getAddress())) return;
                String url ;
                if (coin.getCoin().getUnit().equals("GCX")){
                    url = coin.getAddress();
                }else {
//                    url = coin.getCoin().getName()+"://"+coin.getAddress();
                    url = coin.getCoin().getName()+":"+coin.getAddress();
                }
                saveBitmap = createQRCode(url, Math.min(ivAddress.getWidth(), ivAddress.getHeight()));
                ivAddress.setImageBitmap(saveBitmap);
            }
        });

        SpannableStringBuilder builder = new SpannableStringBuilder(remake_address.getText().toString());
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        builder.setSpan(span,8,18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        remake_address.setText(builder);
    }

    @OnClick({R.id.llCopy, R.id.llAlbum})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llCopy:
                copy();
                break;
            case R.id.llAlbum:
                if (!PermissionUtils.isCanUseStorage(activity))
                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                else try {
                    save();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(activity).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    try {
                        save();
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

    /**
     * 保存到相册
     */
    private void save() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cayman/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (saveBitmap != null) try {
            BitmapUtils.saveBitmapToFile(saveBitmap, file, 100);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            ToastUtils.showToast(getString(R.string.savesuccess));
        }
    }

    /**
     * 复制
     */
    private void copy() {
        CommonUtils.copyText(this, tvAddress.getText().toString());
        ToastUtils.showToast(R.string.copy_success);
    }


    @Override
    protected void loadData() {

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
}
