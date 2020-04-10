package com.spark.szhb_master.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.spark.szhb_master.MyApplication;


/**
 * Created by Administrator on 2018/1/17.
 */

public class SharedPreferenceInstance {
    private SharedPreferences mPreferences;
    private static SharedPreferenceInstance mInstance = null;
    public static final String SP_KEY_ISFIRSTUSE = "SYSTEM_KEY_ISFIRSTUSE";
    public static final String SP_KEY_LANGUAGE = "SP_KEY_LANGUAGE";
    public static final String SP_KEY_MONEY_SHOW_TYPE = "SP_KEY_MONEY_SHOW_TYPE";
    public static final String SP_KEY_MONEY_SHOW_UNIT = "SP_KEY_MONEY_SHOW_UNIT";
    private static final String SP_KEY_LOCK_PWD = "SP_KEY_LOCK_PWD";
    private static final String SP_KEY_IS_NEED_SHOW_LOCK = "SP_KEY_IS_NEED_SHOW_LOCK";
    private static final String SP_KEY_IS_NEED_OPNE = "SP_KEY_IS_NEED_OPNE";
    private static final String SP_KEY_TOKEN = "SP_KEY_TOKEN";
    private static final String HAS_NEW_MESSAGE = "HAS_NEW_MESSAGE";
    private static final String VERSION_CODE = "VERSION_CODE";
    private static final String GOOGLESTATUS = "GOOGLESTATUS"; // 谷歌验证
    private static final String SP_KEY_GOOGLE = "SP_KEY_GOOGLE"; // 谷歌验证lxd
    private static final String SP_KEY_GCX = "SP_KEY_GCX"; // 保存GCX数量
    private static final String SP_KEY_SUAN = "SP_KEY_SUAN"; // 保存GCX数量
    private static final String SP_MSG = "SP_MSG"; // 保存GCX数量

    private SharedPreferenceInstance() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getApp().getApplicationContext());
    }

    public synchronized static SharedPreferenceInstance getInstance() {
        return mInstance == null ? new SharedPreferenceInstance() : mInstance;
    }

    /**
     * 获取是否是第一次使用APP
     **/
    public boolean getIsFirstUse() {
        return mPreferences == null ? true : mPreferences.getBoolean(SP_KEY_ISFIRSTUSE, true);
    }

    /**
     * 保存是否是第一次使用APP
     */
    public void saveIsFirstUse(boolean isFirstUse) {
        if (mPreferences == null) return;
        mPreferences.edit().putBoolean(SP_KEY_ISFIRSTUSE, isFirstUse).apply();
    }

    /**
     * 保存语言偏好  // 1 中文  2 英文  3日文
     *
     * @param languageCode
     */
    public void saveLanguageCode(int languageCode) {
        if (mPreferences == null) return;
        mPreferences.edit().putInt(SP_KEY_LANGUAGE, languageCode).apply();
    }

    /**
     * 获取语言偏好
     *
     * @return
     */
    public int getLanguageCode() {
        //if (mPreferences == null) return 1;
        return mPreferences.getInt(SP_KEY_LANGUAGE, 1);
    }

    /**
     * 获取语言偏好
     *
     * @return
     */
    public synchronized void saveGoogleSatus(int status) {
        if (mPreferences == null) return;
        mPreferences.edit().putInt(GOOGLESTATUS, status).apply();
    }

    /**
     * 保存手势密码
     */
    public synchronized void saveLockPwd(String encryPas) {
        if (mPreferences == null) return;
        mPreferences.edit().putString(SP_KEY_LOCK_PWD, encryPas).apply();
    }

    /**
     * 保存谷歌验证
     */
    public synchronized void saveGoogle(String encryPas) {
        if (mPreferences == null) return;
        mPreferences.edit().putString(SP_KEY_GOOGLE, encryPas).apply();
    }

    /**
     * 获取手势密码
     */
    public synchronized String getLockPwd() {
        return mPreferences == null ? null : mPreferences.getString(SP_KEY_LOCK_PWD, null);
    }
    /**
     * 获取谷歌验证
     */
    public synchronized String getGoogle() {
        return mPreferences == null ? null : mPreferences.getString(SP_KEY_GOOGLE, null);
    }


    /**
     * 保存账户余额显示偏好 // 1 明文显示  2 密文显示
     */
    public void saveMoneyShowtype(int type) {
        if (mPreferences == null) return;
        mPreferences.edit().putInt(SP_KEY_MONEY_SHOW_TYPE, type).apply();
    }

    /**
     * 获取账户余额显示偏好
     */
    public int getMoneyShowType() {
        return mPreferences == null ? 2 : mPreferences.getInt(SP_KEY_MONEY_SHOW_TYPE, 1);
    }

    /**
     * 保存账户余额显示偏好 // 1 CNY  2  JPY
     */
    public void saveMoneyShowUnit(int type) {
        if (mPreferences == null) return;
        mPreferences.edit().putInt(SP_KEY_MONEY_SHOW_UNIT, type).apply();
    }

    /**
     * 获取账户余额显示单位偏好
     */
    public int getMoneyShowUnitType() {
        return mPreferences == null ? 1 : mPreferences.getInt(SP_KEY_MONEY_SHOW_UNIT, 1);
    }

    /**
     * 保存再进入 是否需要显示手势锁
     */
    public void saveIsNeedShowLock(boolean b) {
        if (mPreferences == null) return;
        mPreferences.edit().putBoolean(SP_KEY_IS_NEED_SHOW_LOCK, b).apply();
    }

    /**
     * 获取再进入 是否需要显示手势锁
     */
    public boolean getIsNeedShowLock() {
        return mPreferences == null ? false : mPreferences.getBoolean(SP_KEY_IS_NEED_SHOW_LOCK, false);
    }


    /**
     * 保存打开相机
     */
    public void saveIsOpen(boolean b) {
        if (mPreferences == null) return;
        mPreferences.edit().putBoolean(SP_KEY_IS_NEED_OPNE, b).apply();
    }

    /**
     * 获取再进入 是否需要显示手势锁
     */
    public boolean getIsNeedColse() {
        return mPreferences == null ? false : mPreferences.getBoolean(SP_KEY_IS_NEED_OPNE, true);
    }


    /**
     * 保存验证token
     */
    public void saveToken(String tokenKey) {
        if (mPreferences == null) return;
        mPreferences.edit().putString(SP_KEY_TOKEN, tokenKey).apply();
    }

    /**
     * 获取验证token
     */
    public String getToken() {
        return mPreferences == null ? "" : mPreferences.getString(SP_KEY_TOKEN, "");
    }

    /**
     * 保存新消息提示
     */
    public void saveHasNew(boolean b) {
        if (mPreferences == null) return;
        mPreferences.edit().putBoolean(HAS_NEW_MESSAGE, b).apply();
    }

    /**
     * 获取新消息提示
     */
    public boolean getHasNew() {
        return mPreferences == null ? false : mPreferences.getBoolean(HAS_NEW_MESSAGE, false);
    }

    /**
     * 保存版本号
     */
    public void saveVersion(String code) {
        if (mPreferences == null) return;
        mPreferences.edit().putString(VERSION_CODE, code).apply();
    }

    /**
     * 获取版本号
     */
    public String getVersion() {
        return mPreferences == null ? "" : mPreferences.getString(VERSION_CODE, "V1.0.0");
    }

    /**
     * 获取版本号
     */
    public int getGooglestatus() {
        return mPreferences == null ? 0 : mPreferences.getInt(GOOGLESTATUS, 0);
    }


    /**
     * 保存GCX与算力值
     */
    public void saveGCX(int num,int num_suan) {
        if (mPreferences == null) return;
        mPreferences.edit().putInt(SP_KEY_GCX,num)
                .putInt(SP_KEY_SUAN,num_suan).apply();
    }



    /**
     * 获取GCX与算力值
     */
    public int getGCX() {
        return mPreferences == null ? 0 : mPreferences.getInt(SP_KEY_GCX, 0);
    }


    public int getSUAN() {
        return mPreferences == null ? 0 : mPreferences.getInt(SP_KEY_SUAN, 0);
    }

    public void saveMSG(int msg) {
        if (mPreferences == null) return;
        mPreferences.edit().putInt(SP_MSG,msg).apply();
    }

    public int getMSG() {
        return mPreferences == null ? 0 : mPreferences.getInt(SP_MSG, 0);
    }
}

