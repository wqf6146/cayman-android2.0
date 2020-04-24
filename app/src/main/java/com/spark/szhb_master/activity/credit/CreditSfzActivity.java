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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.base.ActivityManage;
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

import java.io.ByteArrayOutputStream;
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
public class CreditSfzActivity extends BaseActivity implements CreditContract.View {
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

    private int type;
    private ImageView currentImg;
    private File imageFile;
    private String filename = "idCard.jpg";
    private String idCardFront = "";
    private String idCardBack = "";
    private String handHeldIdCard = "";
    private Uri imageUri;
    private CreditContract.Presenter presenter;
    private Credit.DataBean dataBean;


    private String sIvFace,sIvBack,sIvHode,mName,mIdNum;

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
                            saveBitmap(bitmap);
//                            String base64Data = BitmapUtils.imgToBase64(bitmap);
//                            bitmap.recycle();
//                            upLoadBase64("data:image/jpeg;base64," + base64Data);
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
                            saveBitmap(bm);
//                            String base64Data = BitmapUtils.imgToBase64(bm);
//                            bm.recycle();
//                            upLoadBase64("data:image/jpeg;base64," + base64Data);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void saveBitmap(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes=baos.toByteArray();

        Glide.with(this).load(bytes).into(currentImg);
        switch (type) {
            case FACE:
                sIvFace = "data:image/png;base64," + BitmapUtils.imgToBase64(bitmap);
                break;
            case BACK:
                sIvBack = "data:image/png;base64," + BitmapUtils.imgToBase64(bitmap);
                break;
            case HOLD:
                sIvHode = "data:image/png;base64," + BitmapUtils.imgToBase64(bitmap);
                break;
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_credit_sfz;
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
        new CreditPresenter(Injection.provideTasksRepository(CreditSfzActivity.this), this);
        imageFile = FileUtils.getCacheSaveFile(this, filename);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
//            noticeType = bundle.getInt("NoticeType", 0);
//            notice = bundle.getString("Notice");
//            if (noticeType == UNAUDITING) {
//                llNotice.setVisibility(View.GONE);
//            }
            mName = bundle.getString("name");
            mIdNum = bundle.getString("id");
        }

        Intent intent = getIntent();
        if (intent != null){
//            noticeType = intent.getIntExtra("NoticeType", 0);
//            notice = bundle.getString("Notice");
//            if (noticeType == UNAUDITING) {
//                llNotice.setVisibility(View.GONE);
//            }
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
        if (StringUtils.isEmpty( sIvFace, sIvBack, sIvHode)) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else {
            HashMap map = new HashMap<>();
            map.put("type", 0);
            map.put("real_name", mName);
            map.put("id_card", mIdNum);
            map.put("card_front", sIvFace);
            map.put("card_obverse", sIvBack);
            map.put("card_hand", sIvHode);
            presenter.credit(map);
        }
    }

    @Override
    protected void loadData() {
//        if (noticeType != UNAUDITING)
//            presenter.getCreditInfo();
    }


    @Override
    public void setPresenter(CreditContract.Presenter presenter) {
        this.presenter = presenter;
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
    public void doCreditSuccess(String obj) {
        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        ActivityManage.closeActivity(CreditOneActivity.class);
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
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
