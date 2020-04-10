package com.spark.szhb_master.utils;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.entity.Country;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Administrator on 2017/9/1.
 * 通用工具类
 */

public class CommonUtils {
    /**
     * 将指定内容粘贴到剪贴板
     *
     * @param content 剪切内容
     */
    public static void copyText(Context context, String content) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newRawUri("copyLable", Uri.parse(content));
        cm.setPrimaryClip(mClipData);
    }

    /**
     * 获取版本号
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            //第二个参数代表额外的信息，例如获取当前应用中的所有的Activity
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机序列号
     *
     * @return
     */
    public static String getSerialNumber() {
        String serialnum = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception ignored) {
        }
        return serialnum;
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 程序是否在前台运行
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取md5key
     */
    public static String getMd5Key(String username, String pwd) {
        String key = CommonUtils.getSerialNumber() + username + pwd;
        return getMD5(key);
    }

    /**
     * 转化md5
     *
     * @param info
     * @return
     */
    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }


    public static String getCountryNameByLanguageCode(Country country) {
        if (country == null)
            return "中国";
        if (SharedPreferenceInstance.getInstance().getLanguageCode() == 1) {
            return country.getZhName();
        } else if (SharedPreferenceInstance.getInstance().getLanguageCode() == 2) {
            return country.getEnName();
        } else if (SharedPreferenceInstance.getInstance().getLanguageCode() == 3) {
            return country.getJpLanguage();
        }
        return "中国";
    }

    /**
     * 返回版本号
     *
     * @return
     */
    public static String getAppVersionNum() {
        String versionName = "";
        try {
            PackageManager pm = MyApplication.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(MyApplication.getApp().getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 安装
     *
     * @param apkPath
     */
    public static void installAPk(File apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri apkUri = FileUtils.getUriForFile(MyApplication.getApp(), apkPath);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        MyApplication.getApp().startActivity(intent);
    }

    /**
     * 获取交易对单位
     *
     * @param strSymbol
     * @return
     */
    public static String getUnitBySymbol(String strSymbol) {
        if (StringUtils.isNotEmpty(strSymbol)) {
            String splitSymbol = strSymbol.substring(strSymbol.indexOf("/") + 1, strSymbol.length());
            if (StringUtils.isNotEmpty(splitSymbol))
                return splitSymbol;
        }
        return GlobalConstant.CNY;
    }

    /**
     * 获取包名
     *
     * @return
     */
    public static String getAppPackageName() {
        ActivityManager activityManager = (ActivityManager) MyApplication.getApp().getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getPackageName();
    }

    /**
     * 如果版本1 大于 版本2 返回true 否则返回fasle 支持 2.2 2.2.1 比较
     * 支持不同位数的比较  2.0.0.0.0.1  2.0 对比
     *
     * @param v1 版本服务器版本 " 1.1.2 "
     * @param v2 版本 当前版本 " 1.2.1 "
     * @return ture ：需要更新 false ： 不需要更新
     */
    public static boolean compareVersions(String v1, String v2) {
        if (StringUtils.isEmpty(v1) || StringUtils.isEmpty(v2)) {
            return false;
        }
        String[] str1 = v1.split("\\.");
        String[] str2 = v2.split("\\.");

        if (str1.length == str2.length) {
            for (int i = 0; i < str1.length; i++) {
                if (Integer.parseInt(str1[i]) > Integer.parseInt(str2[i])) {
                    return true;
                } else if (Integer.parseInt(str1[i]) < Integer.parseInt(str2[i])) {
                    return false;
                } else if (Integer.parseInt(str1[i]) == Integer.parseInt(str2[i])) {

                }
            }
        } else {
            if (str1.length > str2.length) {
                for (int i = 0; i < str2.length; i++) {
                    if (Integer.parseInt(str1[i]) > Integer.parseInt(str2[i])) {
                        return true;
                    } else if (Integer.parseInt(str1[i]) < Integer.parseInt(str2[i])) {
                        return false;

                    } else if (Integer.parseInt(str1[i]) == Integer.parseInt(str2[i])) {
                        if (str2.length == 1) {
                            continue;
                        }
                        if (i == str2.length - 1) {

                            for (int j = i; j < str1.length; j++) {
                                if (Integer.parseInt(str1[j]) != 0) {
                                    return true;
                                }
                                if (j == str1.length - 1) {
                                    return false;
                                }

                            }
                            return true;
                        }
                    }
                }
            } else {
                for (int i = 0; i < str1.length; i++) {
                    if (Integer.parseInt(str1[i]) > Integer.parseInt(str2[i])) {
                        return true;
                    } else if (Integer.parseInt(str1[i]) < Integer.parseInt(str2[i])) {
                        return false;

                    } else if (Integer.parseInt(str1[i]) == Integer.parseInt(str2[i])) {
                        if (str1.length == 1) {
                            continue;
                        }
                        if (i == str1.length - 1) {
                            return false;

                        }
                    }

                }
            }
        }
        return false;
    }

    /**
     * 检查是否有网络
     */
    public static boolean isNetConnect() {
        boolean isConnect = false;
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        NetworkInfo.State mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if ((wifiState != null && wifiState == NetworkInfo.State.CONNECTED) || (mobileState != null && mobileState == NetworkInfo.State.CONNECTED)) {
            isConnect = true;
        }
        return isConnect;
    }

    /**
     * 根据国家返回相应值
     *
     * @param country
     * @return
     */
    public static String getNameByCode(Country country) {
        if (SharedPreferenceInstance.getInstance().getLanguageCode() == 1) {
            return country.getZhName();
        } else if (SharedPreferenceInstance.getInstance().getLanguageCode() == 2) {
            return country.getEnName();
        } else if (SharedPreferenceInstance.getInstance().getLanguageCode() == 3) {
            return country.getJpLanguage();
        }
        return country.getZhName();
    }

}
