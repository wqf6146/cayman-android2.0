 package com.spark.szhb_master.activity.safe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.GoogleCode;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 谷歌验证
 */
public class GoogleAuthorActivity extends BaseActivity implements GoogleContract.View {
    @BindView(R.id.ivAddress)
    ImageView ivAddress;
    @BindView(R.id.tvAdress)
    TextView tvAddress;
    private GoogleContract.Presenter presenter;
    private int isfirst = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_google_author;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.google_authentication_tag));
        new GooglePresenter(Injection.provideTasksRepository(getApplicationContext()), this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isfirst = bundle.getInt("isfirst");
            if (isfirst == 1){
                setTitle(getString(R.string.google_authentication_modify));
            }
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        presenter.getInfo();
    }

    @OnClick({R.id.tvCopy, R.id.tvNext})
    protected void setOnClickListener(View view) {
        switch (view.getId()) {
            case R.id.tvNext:
                Bundle bundle = new Bundle();
                bundle.putInt("isfirst", isfirst);
                bundle.putString("secret", tvAddress.getText().toString());
                if (StringUtils.isEmpty(tvAddress.getText().toString())){
                    ToastUtils.show("获取谷歌二维码失败", Toast.LENGTH_SHORT);
                }else {
                    showActivity(GoogleCodeActivity.class, bundle, 0);
                }
                break;
            case R.id.tvCopy:
                CommonUtils.copyText(this, tvAddress.getText().toString());
                ToastUtils.showToast(getString(R.string.copy_success));
                break;
        }
    }

    /**
     * 生成二维码
     */
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setPresenter(GoogleContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getInfoSuccess(GoogleCode googleCode) {
        String link = googleCode.getLink();
        Bitmap bitmap = generateBitmap(link, ivAddress.getWidth(), ivAddress.getHeight());
        if (bitmap != null)
            ivAddress.setImageBitmap(bitmap);
        String secret = googleCode.getSecret();
        tvAddress.setText(secret);
    }

    @Override
    public void getInfoFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }
}
