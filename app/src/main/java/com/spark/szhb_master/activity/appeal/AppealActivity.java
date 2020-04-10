package com.spark.szhb_master.activity.appeal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.BitmapUtils;
import com.spark.szhb_master.utils.FileUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.UriUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 申诉
 */
public class AppealActivity extends BaseActivity implements AppealContract.View {
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.etType)
    EditText etType;
    @BindView(R.id.image_one)
    ImageView image_one;
    @BindView(R.id.image_two)
    ImageView image_two;
    @BindView(R.id.image_three)
    ImageView image_three;
    private String orderSn;
    private AppealContract.Presenter presenter;
    private ImageView currentImg;
    private int status;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_appeal;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.order_appeal));
        new AppealPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        imageFile = FileUtils.getCacheSaveFile(this, filename);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderSn = bundle.getString("orderSn");
        }

        bankNames = getResources().getStringArray(R.array.appeal_status);
        etType.setText(bankNames[0]);
    }

    @Override
    protected void setListener() {
        super.setListener();
        etRemark.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(300)});
    }

    private InputFilter inputFilter = new InputFilter() {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtils.showToast(getString(R.string.no_input_emoji));
                return "";
            }
            return null;
        }
    };

    @OnClick({R.id.tvSubmit, R.id.tvCancle, R.id.image_one, R.id.image_two, R.id.image_three, R.id.etType})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvSubmit:
                String remark = etRemark.getText().toString().trim();
                if (StringUtils.isEmpty(remark)) {
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                } else {
                    showCofirmDialog();
                }
                break;
            case R.id.tvCancle:
                finish();
                break;
            case R.id.etType:
                showPayWayDialog();
                break;
            case R.id.image_one:
                actionSheetDialogNoTitle();
                currentImg = image_one;
                num = 1;
                break;
            case R.id.image_two:
                actionSheetDialogNoTitle();
                currentImg = image_two;
                num = 2;
                break;
            case R.id.image_three:
                actionSheetDialogNoTitle();
                currentImg = image_three;
                num = 3;
                break;
        }
    }

    private int num = 1;


    /**
     * 对话框
     */
    private void showCofirmDialog() {
        final MaterialDialog dialog = new MaterialDialog(activity);
        dialog.title(getString(R.string.warm_prompt)).titleTextColor(getResources().getColor(R.color.colorPrimary)).content(getString(R.string.confirm_go_on_appeal_tag)).btnText(getString(R.string.dialog_one_cancel), getString(R.string.continue_tag)).setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("stauts", status + "");
                            jsonObject.put("remark", etRemark.getText().toString());
                            remake = jsonObject.toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        HashMap<String, String> map = new HashMap<>();
                        map.put("orderSn", orderSn);
                        map.put("remark", String.valueOf(remake));
                        map.put("imgUrls", imageurlone + ";" + imageurltwo + ";" + imageurlthree);
                        presenter.appeal(map);
                        dialog.superDismiss();
                    }
                });
        dialog.show();
    }

    private String remake;

    //;
    @Override
    public void setPresenter(AppealContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void appealSuccess(String obj) {
        ToastUtils.showToast(obj);
        finish();
    }

    @Override
    public void appealFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    private NormalListDialog dialog;
    private String[] bankNames;

    /**
     * 选择支付方式
     */
    private void showPayWayDialog() {
        dialog = new NormalListDialog(activity, bankNames);
        dialog.title("请选择申诉类型");
        dialog.titleBgColor(getResources().getColor(R.color.main_head_bg));
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                status = position + 1;
                String bankName = bankNames[position];
                etType.setText(bankName);
                dialog.dismiss();
            }
        });
        dialog.show();
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

    private File imageFile;
    private Uri imageUri;
    private String filename = "appeal.jpg";
    private String url = "";

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalConstant.TAKE_PHOTO:
                    Bitmap bitmap = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), currentImg.getWidth(), currentImg.getHeight());
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
                    Bitmap bm = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), currentImg.getWidth(), currentImg.getHeight());
                    if (GlobalConstant.isUpLoadFile) {
                        upLoadImageFile(bm);
                    } else {
                        upLoadBase64("data:image/jpeg;base64," + BitmapUtils.imgToBase64(bm));
                    }
                    break;
            }
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
        presenter.uploadBase64Pic(map, num);
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
        presenter.uploadImageFile(imageFile, num);
    }

    private String imageurlone = "";
    private String imageurltwo = "";
    private String imageurlthree = "";

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
            case 1:
                imageurlone = obj;
                break;
            case 2:
                imageurltwo = obj;
                break;
            case 3:
                imageurlthree = obj;
                break;
        }

    }
}
