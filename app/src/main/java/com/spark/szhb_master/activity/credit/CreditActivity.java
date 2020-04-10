package com.spark.szhb_master.activity.credit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Credit;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 身份验证
 */
public class CreditActivity extends BaseActivity implements CreditContract.View {
    public static final int FACE = 1;
    public static final int BACK = 2;
    public static final int HOLD = 3;
    public static final int AUDITING_SUCCESS = 6;
    public static final int AUDITING_ING = 7;
    public static final int AUDITING_FILED = 8;
    public static final int UNAUDITING = 9;
    @BindView(R.id.ivIdFace)
    ImageView ivIdFace;
    @BindView(R.id.ivIconFace)
    ImageView ivIconFace;
    @BindView(R.id.ivIdBack)
    ImageView ivIdBack;
    @BindView(R.id.ivIconBack)
    ImageView ivIconBack;
    @BindView(R.id.ivHold)
    ImageView ivHold;
    @BindView(R.id.ivIconHold)
    ImageView ivIconHold;
    @BindView(R.id.tvCredit)
    TextView tvCredit;
    @BindView(R.id.etCountry)
    TextView etCountry;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etIdNumber)
    EditText etIdNumber;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    @BindView(R.id.ivNoticeIcon)
    ImageView ivNoticeIcon;
    @BindView(R.id.llNotice)
    LinearLayout llNotice;
    @BindView(R.id.llFace)
    LinearLayout llFace;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.llHold)
    LinearLayout llHold;
    @BindView(R.id.svContainer)
    ScrollView svContainer;
    private int type;
    private int noticeType;
    private ImageView currentImg;
    private File imageFile;
    private String filename = "idCard.jpg";
    private String idCardFront = "";
    private String idCardBack = "";
    private String handHeldIdCard = "";
    private String notice;
    private Uri imageUri;
    private CreditContract.Presenter presenter;
    private Credit.DataBean dataBean;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalConstant.TAKE_PHOTO:
                    try {
                        Bitmap bitmap = BitmapUtils.loadBitmap(new FileInputStream(imageFile), currentImg.getWidth(), currentImg.getHeight());
                        BitmapUtils.saveBitmapToFile(bitmap, imageFile, 100);
                        if (GlobalConstant.isUpLoadFile) {
                            upLoadImageFile(bitmap);
                        } else {
                            String base64Data = BitmapUtils.imgToBase64(bitmap);
                            bitmap.recycle();
                            upLoadBase64("data:image/jpeg;base64," + base64Data);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case LoginActivity.RETURN_LOGIN:
                    if (MyApplication.getApp().isLogin()) hideToLoginView();
                    break;
                case GlobalConstant.CHOOSE_ALBUM:
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
                    try {
                        Bitmap bm = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), currentImg.getWidth(), currentImg.getHeight());
                        BitmapUtils.saveBitmapToFile(bm, imageFile, 100);
                        if (GlobalConstant.isUpLoadFile) {
                            upLoadImageFile(bm);
                        } else {
                            String base64Data = BitmapUtils.imgToBase64(bm);
                            bm.recycle();
                            upLoadBase64("data:image/jpeg;base64," + base64Data);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_credit;
    }

    @Override
    protected ViewGroup getEmptyView() {
        return llContainer;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void onStart() {
        isNeedChecke = false;
        super.onStart();
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.my_credit));
        tvGoto.setVisibility(View.INVISIBLE);
        new CreditPresenter(Injection.provideTasksRepository(CreditActivity.this), this);
        imageFile = FileUtils.getCacheSaveFile(this, filename);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            noticeType = bundle.getInt("NoticeType", 0);
            notice = bundle.getString("Notice");
            if (noticeType == UNAUDITING) {
                llNotice.setVisibility(View.GONE);
            }
        }
        Intent intent = getIntent();
        if (intent != null){
            noticeType = intent.getIntExtra("NoticeType", 0);
            notice = bundle.getString("Notice");
            if (noticeType == UNAUDITING) {
                llNotice.setVisibility(View.GONE);
            }
        }

    }

    @OnClick({R.id.tvCredit, R.id.rvHold, R.id.rvBack, R.id.rvFace})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvCredit:
                credit();
                return;
            case R.id.rvHold:
                type = HOLD;
                currentImg = ivHold;
                break;
            case R.id.rvBack:
                type = BACK;
                currentImg = ivIdBack;
                break;
            case R.id.rvFace:
                type = FACE;
                currentImg = ivIdFace;
                break;
        }
        actionSheetDialogNoTitle();
    }

    private void credit() {
        String idCard = etIdNumber.getText().toString().trim();
        String realName = etName.getText().toString().trim();
        if (StringUtils.isEmpty(realName, idCard, idCardFront, idCardBack, handHeldIdCard)) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else if (idCard.contains("****")){
            ToastUtils.showToast("请重新输入身份证号");
        } else if (!StringUtils.isIDCard(idCard)) {
            ToastUtils.showToast(getString(R.string.idcard_diff));
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("realName", realName);
            map.put("idCard", idCard);
            map.put("idCardFront", idCardFront);
            map.put("idCardBack", idCardBack);
            map.put("handHeldIdCard", handHeldIdCard);
            map.put("type", "0");
            presenter.credit(map);
        }
    }

    @Override
    protected void loadData() {
        if (noticeType != UNAUDITING)
            presenter.getCreditInfo();
    }


    @Override
    public void setPresenter(CreditContract.Presenter presenter) {
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
        presenter.uploadBase64Pic(map, type);
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
        presenter.uploadImageFile(imageFile, type);
    }

    @Override
    public void uploadBase64PicSuccess(String obj, int type) {
        if (StringUtils.isEmpty(obj)) {
            ToastUtils.showToast(getString(R.string.empty_address));
            return;
        }
        ToastUtils.showToast(getString(R.string.upload_success));
        if (!obj.contains("http")) {
            obj = UrlFactory.getHost() + "/" + obj;
        }
        Glide.with(this).load(obj).into(currentImg);
        switch (type) {
            case FACE:
                idCardFront = obj;
                break;
            case BACK:
                idCardBack = obj;
                break;
            case HOLD:
                handHeldIdCard = obj;
                break;
        }
    }

    @Override
    public void doCreditSuccess(String obj) {
        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void getCreditInfoSuccess(Credit.DataBean obj) {
        svContainer.setVisibility(View.VISIBLE);
        Credit credit = new Credit();
        credit.setData(obj);
        dataBean = credit.getData();
        if (dataBean != null) {
            etName.setText(dataBean.getRealName());
            if (StringUtils.isNotEmpty(dataBean.getIdCard()))
                etIdNumber.setText(dataBean.getIdCard().substring(0, 4) + "**********" + dataBean.getIdCard().substring(14));
            idCardFront = dataBean.getIdentityCardImgFront();
            Glide.with(this).load(idCardFront).into(ivIdFace);
            idCardBack = dataBean.getIdentityCardImgReverse();
            Glide.with(this).load(idCardBack).into(ivIdBack);
            handHeldIdCard = dataBean.getIdentityCardImgInHand();
            Glide.with(this).load(handHeldIdCard).into(ivHold);
            ivIconFace.setVisibility(View.GONE);
            ivIconBack.setVisibility(View.GONE);
            ivIconHold.setVisibility(View.GONE);
        }

        if (noticeType == CreditActivity.AUDITING_ING) {
            tvNotice.setText(R.string.unverified_notice);
            ivNoticeIcon.setImageResource(R.mipmap.icon_identify_ing);
            tvCredit.setClickable(false);
            tvCredit.setBackgroundColor(getResources().getColor(R.color.grey_a5a5a5));
            etName.setEnabled(false);
            etIdNumber.setEnabled(false);
            ivIconFace.setVisibility(View.GONE);
            ivIconBack.setVisibility(View.GONE);
            ivIconHold.setVisibility(View.GONE);
        } else if (noticeType == CreditActivity.AUDITING_FILED) {
            tvNotice.setText(getString(R.string.creditfail_notice) + notice);
            ivNoticeIcon.setImageResource(R.mipmap.icon_prompt);
            ivIconFace.setVisibility(View.VISIBLE);
            ivIconBack.setVisibility(View.VISIBLE);
            ivIconHold.setVisibility(View.VISIBLE);
        } else if (noticeType == CreditActivity.AUDITING_SUCCESS) {
            tvNotice.setText(R.string.certifySuccessful);
            llNotice.setBackgroundColor(getResources().getColor(R.color.btn_normal));
            tvNotice.setTextColor(getResources().getColor(R.color.white));
            ivNoticeIcon.setVisibility(View.GONE);
            etName.setEnabled(false);
            etIdNumber.setEnabled(false);
            llFace.setVisibility(View.GONE);
            llBack.setVisibility(View.GONE);
            llHold.setVisibility(View.GONE);
            tvCredit.setVisibility(View.GONE);
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

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_CAMERA:
                    actionSheetDialogNoTitle();
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

}
