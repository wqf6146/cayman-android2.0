package com.spark.szhb_master;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.FileUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.Config;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.litepal.LitePal;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by pc on 2017/3/8.
 */
public class MyApplication extends Application {
    private boolean isReleased = false; // 是否发布了
    private boolean isLoginStatusChange = false; // 当前用户信息是否发生改变
    public static MyApplication app;
    private User currentUser = new User();
    private TextView tvToast; // Toast
    private int mWidth; // 当前手机屏幕的宽高
    private int mHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initView();
                getDisplayMetric();
                getCurrentUserFromFile();
                LitePal.initialize(MyApplication.getApp());
                ZXingLibrary.initDisplayOpinion(MyApplication.getApp());
                x.Ext.init(MyApplication.getApp());
            }
        }).start();

        UMShareAPI.get(this);//初始化sdk
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;

    }

    private void initView() {
        tvToast = (TextView) View.inflate(app, R.layout.my_toast, null);
    }

    public boolean isLogin() {
        if (getCurrentUser() == null) return false;
        return !StringUtils.isEmpty(getCurrentUser().getToken());
    }

    /**
     * 获取屏幕的宽高
     */
    private void getDisplayMetric() {
        mWidth = getResources().getDisplayMetrics().widthPixels;
        mHeight = getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取程序的Application对象
     */
    public static MyApplication getApp() {
        return app;
    }

    /**
     * 重新登录
     */
    public void loginAgain(BaseActivity activity) {
        deleteCurrentUser();
        activity.startActivityForResult(new Intent(activity, LoginActivity.class), LoginActivity.RETURN_LOGIN);
        //10.19改动
        if (StringUtils.isNotEmpty(activity.getClass().toString()) && activity.getClass().toString().equals(MainActivity.class.toString())) {
            return;
        }
        activity.finish();
    }

    /**
     * 重新登录
     */
    public void loginAgain(Fragment fragment) {
        //10.19改动
//        deleteCurrentUser();
//        fragment.startActivityForResult(new Intent(fragment.getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);

        Activity activity = fragment.getActivity();
        deleteCurrentUser();
        fragment.startActivityForResult(new Intent(fragment.getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        if (StringUtils.isNotEmpty(activity.getClass().toString()) && activity.getClass().toString().equals(MainActivity.class.toString())) {
            return;
        }
        activity.finish();
    }


    public synchronized void saveCurrentUser() {
        try {
            File file = FileUtils.getLongSaveFile(this, "User", GlobalConstant.UserSaveFileName);
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(currentUser);
            LogUtils.i("save token==" + currentUser.getToken());
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地存储的文件
     */
    public void deleteCurrentUser() {
        this.currentUser = null;
        File file = FileUtils.getLongSaveFile(this, "User", GlobalConstant.UserSaveFileName);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public void getCurrentUserFromFile() {
        try {
            File file = new File(FileUtils.getLongSaveDir(this, "User"), GlobalConstant.UserSaveFileName);
            if (file != null && file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                this.currentUser = (User) ois.readObject();
                if (this.currentUser == null) {
                    this.currentUser = new User();
                }
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public int getmWidth() {
        return mWidth;
    }
    public int getmHeight() {
        return mHeight;
    }



    public User getCurrentUser() {
        return currentUser == null ? currentUser = new User() : currentUser;
    }

    public synchronized void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        saveCurrentUser();
    }

    public synchronized void updateCurrentUser(User currentUser){
        User localUser = getCurrentUser();

        
    }

    public boolean isLoginStatusChange() {
        return isLoginStatusChange;
    }

    public void setLoginStatusChange(boolean loginStatusChange) {
        isLoginStatusChange = loginStatusChange;
    }

    {
        PlatformConfig.setWeixin("wx9f595f5316941e1f", "490c07ebfb2a50c3eabb625123bda6a8");
//        PlatformConfig.setWeixin("wx069b07a808e5b1de", "cd658bc42cad59911cbe099175420879");//lxd
    }
}
