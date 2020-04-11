package com.spark.szhb_master.activity.myinfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.DialogTure;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.CopyToast;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * Created by Administrator on 2018/10/11 0011.
 */
public class NickNameActivity extends BaseActivity implements NickNameContract.View {

    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.close_img)
    ImageView close_img;
    @BindView(R.id.tvEdit)
    TextView tvEdit;

    private NickNameContract.Presenter presenter;
    private String nickname;

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.nick_name_layout;
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.modifyname));
        tvGoto.setVisibility(View.VISIBLE);
        setEditTextInputSpeChat(etCode);
        tvGoto.setVisibility(View.INVISIBLE);
        new NikeNamePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Intent intent = getIntent();
        nickname = intent.getStringExtra("name");
        etCode.setText(nickname);
        etCode.setSelection(etCode.getText().length());
    }

    @OnClick({R.id.close_img, R.id.tvEdit})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.close_img:
                etCode.setText("");
                break;
            case R.id.tvEdit:
                if (StringUtils.isEmpty(etCode.getText().toString())) {
                    ToastUtils.show(getResources().getString(R.string.username_notempty), Toast.LENGTH_SHORT);
                }else if (etCode.getText().toString().length() > 16){
                    ToastUtils.show(getResources().getString(R.string.username_notlong), Toast.LENGTH_SHORT);
                }else if (etCode.getText().toString().length() < 3){
                    ToastUtils.show(getResources().getString(R.string.username_notthree), Toast.LENGTH_SHORT);
                } else if (etCode.getText().toString().equals(nickname)){
                    ToastUtils.show(getResources().getString(R.string.username_notsame), Toast.LENGTH_SHORT);
                } else {
                    show_dialog();
                }
                break;
        }
    }

    private void show_dialog() {
        final DialogTure dialogTure = new DialogTure(this);
        dialogTure.show();
        dialogTure.cometwo().setText(getResources().getString(R.string.only_once) + etCode.getText().toString() + getResources().getString(R.string.only_once_wen));

        dialogTure.go_up().setText(getResources().getString(R.string.modification));
        dialogTure.cancle().setText(getResources().getString(R.string.cancle));

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
                HashMap<String, String> map = new HashMap<>();
                map.put("username", etCode.getText().toString());
                presenter.getNikeName(map);
                dialogTure.dismiss();
            }
        });


    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText EditText输入框
     */
    public static void setEditTextInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    @Override
    public void getNikeNameSuccess() {

        User user = MyApplication.getApp().getCurrentUser();
        user.setNick_name(etCode.getText().toString());
        MyApplication.getApp().setCurrentUser(user);

        setResult(RESULT_OK);
        CopyToast.showText(NickNameActivity.this, getResources().getString(R.string.savesuccess));
        finish();

    }


    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void setPresenter(NickNameContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
