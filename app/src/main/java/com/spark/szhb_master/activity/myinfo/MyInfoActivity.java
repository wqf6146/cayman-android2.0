package com.spark.szhb_master.activity.myinfo;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.account_pwd.AccountPwdActivity;
import com.spark.szhb_master.activity.bind_account.BindAccountActivity;
import com.spark.szhb_master.activity.bind_email.BindEmailActivity;
import com.spark.szhb_master.activity.bind_email.EmailActivity;
import com.spark.szhb_master.activity.bind_phone.BindPhoneActivity;
import com.spark.szhb_master.activity.bind_phone.PhoneViewActivity;
import com.spark.szhb_master.activity.credit.CreditActivity;
import com.spark.szhb_master.activity.edit_login_pwd.EditLoginPwdActivity;
import com.spark.szhb_master.dialog.DialogTure;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.utils.CopyToast;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.ui.CircleImageView;
import com.spark.szhb_master.utils.BitmapUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.FileUtils;
import com.spark.szhb_master.utils.PermissionUtils;
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

import static com.spark.szhb_master.R.id.etCode;

public class MyInfoActivity extends BaseActivity implements MyInfoContract.View{
    @BindView(R.id.llAccountPwd)
    LinearLayout llAccountPwd;
    @BindView(R.id.ivHeader)
    CircleImageView ivHeader;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvLoginPwd)
    TextView tvLoginPwd;
    @BindView(R.id.tvAcountPwd)
    TextView tvAcountPwd;
    @BindView(R.id.tvIdCard)
    TextView tvIdCard;
    @BindView(R.id.llPhone)
    LinearLayout llPhone;
    @BindView(R.id.llEmail)
    LinearLayout llEmail;
    @BindView(R.id.llLoginPwd)
    LinearLayout llLoginPwd;
    @BindView(R.id.llIdCard)
    LinearLayout llIdCard;
    @BindView(R.id.etAccount)
    TextView tvAccount;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;
    @BindView(R.id.llNickName)
    LinearLayout llNickName;
    @BindView(R.id.nike_img)
    ImageView nike_img;
    private File imageFile;
    private String filename = "header.jpg";
    private Uri imageUri;
    private String url;
    private MyInfoContract.Presenter presenter;
    private SafeSetting safeSetting;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalConstant.TAKE_PHOTO:
                    takePhotoReturn(resultCode, data);
                    break;
                case GlobalConstant.CHOOSE_ALBUM:
                    choseAlbumReturn(resultCode, data);
                    break;
                case 0: // 重新请求设置参数
                    presenter.safeSetting();
                    break;
                case 1: // 修改了登录密码
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_CAMERA:
                    startCamera();
                    break;
                case GlobalConstant.PERMISSION_STORAGE:
                    chooseFromAlbum();
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_CAMERA:
                    ToastUtils.showToast(getString(R.string.camera_permission));
                    break;
                case GlobalConstant.PERMISSION_STORAGE:
                    ToastUtils.showToast(getString(R.string.storage_permission));
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 相机
     */
    private void startCamera() {
        if (imageFile == null) {
            ToastUtils.showToast(getString(R.string.unknown_error));
            return;
        }
        imageUri = FileUtils.getUriForFile(this, imageFile);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, GlobalConstant.TAKE_PHOTO);
    }

    /**
     * 相册
     */
    private void chooseFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GlobalConstant.CHOOSE_ALBUM);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.account_settings));
        tvGoto.setVisibility(View.INVISIBLE);
        new MyInfoPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        imageFile = FileUtils.getCacheSaveFile(this, filename);
    }

    private String comefrom;
    @Override
    protected void loadData() {
        presenter.safeSetting();
    }

    @OnClick({R.id.ivHeader, R.id.llPhone, R.id.llEmail, R.id.llAccount, R.id.llLoginPwd, R.id.llIdCard, R.id.llAccountPwd,R.id.llNickName})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (v.getId() != R.id.ivHeader && safeSetting == null) {
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.ivHeader:
                actionSheetDialogNoTitle();
                break;
            case R.id.llPhone:
                if (safeSetting.getPhoneVerified() == 0) {
                    showActivity(BindPhoneActivity.class, null, 0);
                } else {
                    bundle.putString("phone", safeSetting.getMobilePhone());
                    showActivity(PhoneViewActivity.class, bundle);
                }
                break;
            case R.id.llEmail:
                if (safeSetting.getEmailVerified() == 0) {
                    showActivity(BindEmailActivity.class, null, 0);
                } else {
                    bundle.putString("email", safeSetting.getEmail());
                    showActivity(EmailActivity.class, bundle);
                }
                break;
            case R.id.llAccount:
                if (safeSetting.getRealVerified() == 0) {
                    ShiMingDialog shiMingDialog = new ShiMingDialog(this);
                    String name = safeSetting.getRealNameRejectReason();
                    if (safeSetting.getRealVerified() == 0) {
                        if (safeSetting.getRealAuditing() == 1) {
                            shiMingDialog.setEntrust(7, name,1);
                        } else {
                            if (safeSetting.getRealNameRejectReason() != null)
                                shiMingDialog.setEntrust(8, name,1);
                            else
                                shiMingDialog.setEntrust(9, name,1);
                        }
                    } else {
                        shiMingDialog.setEntrust(6, name,1);
                    }
                    shiMingDialog.show();
                }else if (safeSetting.getFundsVerified() == 0){
                    showzijmm();
                }else {
                    showActivity(BindAccountActivity.class, null);
                }
//                if (safeSetting.getRealVerified() == 1 && safeSetting.getFundsVerified() == 1) {
//                    showActivity(BindAccountActivity.class, null);
//                } else {
//                    ToastUtils.showToast(getString(R.string.password_realname));
//                }
                break;
            case R.id.llLoginPwd:
                if (safeSetting.getPhoneVerified() == 0) {
                    ToastUtils.showToast(getString(R.string.binding_phone_first));
                } else {
                    bundle.putString("phone", safeSetting.getMobilePhone());
                    showActivity(EditLoginPwdActivity.class, bundle, 1);
                }
                break;
            case R.id.llIdCard:
                bundle.putString("Notice", safeSetting.getRealNameRejectReason());
                if (safeSetting.getRealVerified() == 0) {
                    if (safeSetting.getRealAuditing() == 1) {
                        bundle.putInt("NoticeType", CreditActivity.AUDITING_ING);
                    } else {
                        if (safeSetting.getRealNameRejectReason() != null)
                            bundle.putInt("NoticeType", CreditActivity.AUDITING_FILED);
                        else
                            bundle.putInt("NoticeType", CreditActivity.UNAUDITING);
                    }
                } else {
                    bundle.putInt("NoticeType", CreditActivity.AUDITING_SUCCESS);
                }
                showActivity(CreditActivity.class, bundle, 0);
                break;
            case R.id.llAccountPwd:
                if (safeSetting.getFundsVerified() == 0) { // 设置资金密码
                    bundle.putBoolean("isSet", true);
                }
                showActivity(AccountPwdActivity.class, bundle, 0);
                break;
            case R.id.llNickName:
                if (safeSetting.isCanChangeUsername()){
                    bundle.putString("name", safeSetting.getUsername());
                    showActivity(NickNameActivity.class, bundle, 0);
                }else {
                    CopyToast.showText(this,getResources().getString(R.string.not_editable));
                }
                break;
        }
    }

    private void showzijmm() {
        final DialogTure dialogTure = new DialogTure(this);
        dialogTure.show();
        dialogTure.cometwo().setVisibility(View.GONE);

        dialogTure.go_up().setText(getResources().getString(R.string.go_to_certification));
        dialogTure.cancle().setText(getResources().getString(R.string.cancle));
        dialogTure.name_title().setText(getResources().getString(R.string.not_funding));
        dialogTure.name_title().setTextSize(18);

        dialogTure.cancle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTure.dismiss();
            }
        });

        dialogTure.img_close().setVisibility(View.INVISIBLE);
        dialogTure.go_up().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSet", true);
                showActivity(AccountPwdActivity.class, bundle, 0);
                dialogTure.dismiss();
            }
        });
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

    @Override
    public void setPresenter(MyInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }


    /**
     * 相册选取返回
     *
     * @param resultCode
     * @param data
     */
    private void choseAlbumReturn(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        imageUri = data.getData();
        if (Build.VERSION.SDK_INT >= 19)
            imageFile = UriUtils.getUriFromKitKat(this, imageUri);
        else
            imageFile = UriUtils.getUriBeforeKitKat(this, imageUri);
        if (imageFile == null) {
            ToastUtils.showToast(getString(R.string.library_file_exception));
            return;
        }
        Bitmap bm = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), ivHeader.getWidth(), ivHeader.getHeight());
        if (GlobalConstant.isUpLoadFile) {
            upLoadImageFile(bm);
        } else {
            upLoadBase64("data:image/jpeg;base64," + BitmapUtils.imgToBase64(bm));
        }
    }

    /**
     * 拍照返回
     *
     * @param resultCode
     * @param data
     */
    private void takePhotoReturn(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        Bitmap bitmap = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), ivHeader.getWidth(), ivHeader.getHeight());
        if (GlobalConstant.isUpLoadFile) {
            upLoadImageFile(bitmap);
        } else {
            upLoadBase64("data:image/jpeg;base64," + BitmapUtils.imgToBase64(bitmap));
        }
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


    @Override
    public void uploadBase64PicSuccess(String obj) {
        url = obj;
        HashMap<String, String> map = new HashMap<>();
        map.put("url", obj);
        presenter.avatar(map);
    }

    @Override
    public void avatarSuccess(String obj) {
        MyApplication.getApp().getCurrentUser().setAvatar(url);
        MyApplication.getApp().saveCurrentUser();
        Glide.with(this).load(url).into(ivHeader);
        setResult(RESULT_OK);
    }

    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        this.safeSetting = obj;
        fillViews();
    }

    /**
     * 根据安全参数显示
     */
    private void fillViews() {
        Glide.with(getApplicationContext()).load(StringUtils.isEmpty(safeSetting.getAvatar()) ? R.mipmap.icon_default_header_grey : safeSetting.getAvatar()).into(ivHeader);
        tvPhone.setText(safeSetting.getPhoneVerified() == 0 ? R.string.unbound : R.string.bound);
        tvPhone.setEnabled(safeSetting.getPhoneVerified() == 0);
        tvEmail.setText(safeSetting.getEmailVerified() == 0 ? R.string.unbound : R.string.bound);
        tvEmail.setEnabled(safeSetting.getEmailVerified() == 0);
        tvAcountPwd.setText(safeSetting.getFundsVerified() == 0 ? R.string.not_set : R.string.had_set);
        tvAcountPwd.setEnabled(safeSetting.getFundsVerified() == 0);
        tvAccount.setText(safeSetting.getAccountVerified() == 0 ? R.string.not_set : R.string.had_set);
        tvAccount.setEnabled(safeSetting.getAccountVerified() == 0);
        tvLoginPwd.setText(safeSetting.getLoginVerified() == 0 ? R.string.not_set : R.string.had_set);
        tvLoginPwd.setEnabled(safeSetting.getLoginVerified() == 0);

        nickname.setText(safeSetting.getUsername());
        if (safeSetting.isCanChangeUsername()){
        }else {
            nike_img.setVisibility(View.GONE);
        }


        if (safeSetting.getRealVerified() == 0) {
            if (safeSetting.getRealAuditing() == 1) {
                tvIdCard.setText(R.string.creditting);
            } else {
                if (safeSetting.getRealNameRejectReason() != null)
                    tvIdCard.setText(R.string.creditfail);
                else
                    tvIdCard.setText(R.string.unverified);
            }
        } else {
            tvIdCard.setText(R.string.verification);
        }
        tvIdCard.setEnabled(safeSetting.getRealVerified() == 0);
    }


    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

}
