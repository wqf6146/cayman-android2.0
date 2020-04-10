package com.spark.szhb_master.activity.bind_account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.AccountSetting;
import com.spark.szhb_master.utils.BitmapUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.FileUtils;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.UriUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 绑定支付宝/绑定微信
 * Created by Administrator on 2018/5/2 0002.
 */

public class BindAliActivity extends BaseActivity implements BindAccountContact.AliView {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etNewPwd)
    EditText etNewPwd;
    @BindView(R.id.llQRCode)
    LinearLayout llQRCode;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.ivIdFace)
    ImageView ivIdFace;
    @BindView(R.id.llAli)
    LinearLayout llAli;
    @BindView(R.id.llWeChat)
    LinearLayout llWeChat;
    @BindView(R.id.etWechatAccount)
    EditText etWechatAccount;
    private BindAccountContact.AliPresenter presenter;
    private AccountSetting setting;
    private File imageFile;
    private Uri imageUri;
    private String filename = "header.jpg";
    private String url = "";
    private boolean isAli;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalConstant.TAKE_PHOTO:
                    Bitmap bitmap = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), ivIdFace.getWidth(), ivIdFace.getHeight());
                    if (GlobalConstant.isUpLoadFile) {
                        upLoadImageFile(bitmap);
                    } else {
                        upLoadBase64("data:image/jpeg;base64," + BitmapUtils.imgToBase64(bitmap));
                    }
                    break;
                case GlobalConstant.CHOOSE_ALBUM:
                    imageUri = data.getData();
                    if (Build.VERSION.SDK_INT >= 19)
                        imageFile = UriUtils.getUriFromKitKat(this, imageUri);
                    else
                        imageFile = UriUtils.getUriBeforeKitKat(this, imageUri);
                    if (imageFile == null) {
                        ToastUtils.showToast(getString(R.string.library_file_exception));
                        return;
                    }
                    Bitmap bm = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), ivIdFace.getWidth(), ivIdFace.getHeight());
                    if (GlobalConstant.isUpLoadFile) {
                        upLoadImageFile(bm);
                    } else {
                        upLoadBase64("data:image/jpeg;base64," + BitmapUtils.imgToBase64(bm));
                    }
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_ali;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        tvGoto.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.llQRCode, R.id.tvConfirm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llQRCode:
                actionSheetDialogNoTitle();
                break;
            case R.id.tvConfirm:
                confirm();
                break;
        }
    }


    @Override
    protected void initData() {
        super.initData();
        new BindAliPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        imageFile = FileUtils.getCacheSaveFile(this, filename);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            setting = (AccountSetting) bundle.getSerializable("accountSetting");
            isAli = bundle.getBoolean("isAli");
            setViewByType();
            etName.setText(setting.getRealName());
        }
    }

    /**
     * 根据类型显示view
     */
    private void setViewByType() {
        if (isAli) {
            setTitle(getString(R.string.alipay_binding));
            if (setting.getAliVerified() == 1) {
                etAccount.setText(setting.getAlipay().getAliNo());
                Glide.with(this).load(setting.getAlipay().getQrCodeUrl()).into(ivIdFace);

                tvTitle.setText(getString(R.string.text_change) + getString(R.string.ali_account));
            } else {
                tvTitle.setText(getString(R.string.binding) + getString(R.string.ali_account));
            }
            llAli.setVisibility(View.VISIBLE);
            llWeChat.setVisibility(View.GONE);
        } else {
            setTitle(getString(R.string.wechat_binding));
            if (setting.getWechatVerified() == 1) {
                etWechatAccount.setText(setting.getWechatPay().getweChat());
                Glide.with(this).load(setting.getWechatPay().getQrWeCodeUrl()).into(ivIdFace);
                tvTitle.setText(getString(R.string.text_change) + getString(R.string.weichat_account));
            } else
                tvTitle.setText(getString(R.string.binding) + getString(R.string.weichat_account));
            llAli.setVisibility(View.GONE);
            llWeChat.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setPresenter(BindAccountContact.AliPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * 上传base64位头像
     *
     * @param s
     */
    private void upLoadBase64(String s) {
        HashMap<String, String> map = new HashMap<>();
        map.put("base64Data", s);
        presenter.uploadBase64Pic(map);
    }

    /**
     * 上传base64位头像
     *
     * @param bitmap
     */
    private void upLoadImageFile(Bitmap bitmap) {
        try {
            BitmapUtils.saveBitmapToFile(bitmap, imageFile, 80);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.recycle();
        presenter.uploadImageFile(imageFile);
    }

    /**
     * 提交
     */
    private void confirm() {
        String account = "";
        if (isAli) {
            account = etAccount.getText().toString().trim();
        }
        else{
            account = etWechatAccount.getText().toString().trim();
        }
        String name = etName.getText().toString();
        String pwd = etNewPwd.getText().toString();
        if (StringUtils.isEmpty(name, account, pwd)) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else if (StringUtils.isEmpty( url)){
            ToastUtils.showToast("请上传图片");
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("jyPassword", pwd);
            map.put("realName", name);
            map.put("qrCodeUrl", url);
            if (isAli) {
                map.put("ali", account);
                if (setting.getAliVerified() == 1) {
                    presenter.getUpdateAli(map);
                } else {
                    presenter.getBindAli(map);
                }
            } else {
                map.put("wechat", account);
                if (setting.getWechatVerified() == 1) {
                    presenter.getUpdateWeChat(map);
                } else {
                    presenter.getBindWeChat(map);
                }
            }

        }
    }

    @Override
    public void doBindAliOrWechatSuccess(String obj) {
        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void doUpdateAliOrWechatSuccess(String obj) {
        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void uploadBase64PicSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            url = obj;
            if (!obj.contains("http")) {
                obj = UrlFactory.getHost() + "/" + obj;
            }
            Glide.with(this).load(obj).into(ivIdFace);
            ToastUtils.showToast(getString(R.string.upload_success));
        } else {
            ToastUtils.showToast(getString(R.string.empty_address));
        }
    }

    /**
     * 选择头像弹框
     */
    private void actionSheetDialogNoTitle() {
        final String[] stringItems = {getString(R.string.photograph), getString(R.string.album)};
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (!PermissionUtils.isCanUseCamera(activity)) {
                            AndPermission.with(activity).requestCode(GlobalConstant.PERMISSION_CAMERA).permission(Permission.CAMERA).callback(permissionListener).start();
                        } else {
                            startCamera();
                        }
                        break;
                    case 1:
                        if (!PermissionUtils.isCanUseStorage(activity)) {
                            AndPermission.with(activity).requestCode(GlobalConstant.PERMISSION_STORAGE).permission(Permission.STORAGE).callback(permissionListener).start();
                        } else {
                            chooseFromAlbum();
                        }
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_CAMERA:
                    startCamera();
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_CAMERA:
                    ToastUtils.showToast(getString(R.string.camera_permission));
                    break;
            }
        }
    };

    /**
     * 拍照
     */
    private void startCamera() {
        if (imageFile == null) {
            ToastUtils.showToast(getString(R.string.unknown_error));
            return;
        }
        SharedPreferenceInstance.getInstance().saveIsOpen(false);
        imageUri = FileUtils.getUriForFile(this, imageFile);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, GlobalConstant.TAKE_PHOTO);
    }

    /**
     * 相册
     */
    private void chooseFromAlbum() {
        SharedPreferenceInstance.getInstance().saveIsOpen(false);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GlobalConstant.CHOOSE_ALBUM);
    }

    @Override
    protected void onDestroy() {
        SharedPreferenceInstance.getInstance().saveIsOpen(true);
        super.onDestroy();
    }
}
