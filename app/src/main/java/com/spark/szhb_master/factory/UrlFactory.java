package com.spark.szhb_master.factory;

/**
 * Created by Administrator on 2018/1/29.
 */
public class UrlFactory {
//    private static final String host = "http://csapi.njpsd.com";

    private static final String host = "https://api.caymanex.pro";
    private static final String newhost = "http://39.98.150.252:8080";
//    private static final String host = "http://api.cex.wxmarket.cn";

    public static String getHost() {
        return host;
    }

    public static String getRateUrl() {
        return host + "/market/exchange-rate/usd-cny";
    } // 获取汇率

    public static String getPhoneCodeUrl() {
        return host + "/uc/mobile/code";
    } // 手机验证码

    public static String getUserInfoUrl() {
        return newhost + "/user/info";
    }

    //获取配对列表
    public static String getMatchUrl() {
        return host + "/uc/asset/match/overview";
    }

    public static String getWalletUrl() {
        return host + "/uc/asset/wallet/";
    }

    //获取配对历史
    public static String getMatchHisUrl() {
        return host + "/uc/asset/transaction/all";
    }

    public static String getAllUrl() {
        return host + "/otc/coin/all";
    }

    public static String getAdvertiseUrl() {
        return host + "/otc/advertise/page";
    }

    public static String getCountryUrl() {
        return host + "/uc/support/country";
    }

    public static String getReleaseAdUrl() {
        return host + "/otc/advertise/create";
    }

    public static String getUploadPicUrl() {
        return host + "/uc/upload/oss/base64";
    }

    public static String getCreditUrl() {
        return host + "/uc/approve/real/name";
    }

    public static String getAccountPwdUrl() {
        return host + "/uc/approve/transaction/password";
    }

    public static String getAllAdsUrl() {
        return host + "/otc/advertise/all";
    }

    public static String getReleaseUrl() {
        return host + "/otc/advertise/on/shelves";
    }

    public static String getDeleteAdsUrl() {
        return host + "/otc/advertise/delete";
    }

    public static String getOffShelfUrl() {
        return host + "/otc/advertise/off/shelves";
    }

    public static String getAdDetailUrl() {
        return host + "/otc/advertise/detail";
    }

    public static String getUpdateAdUrl() {
        return host + "/otc/advertise/update";
    }

    public static String getC2CInfoUrl() {
        return host + "/otc/order/pre";
    }

    public static String getC2CBuyUrl() {
        return host + "/otc/order/buy";
    }

    public static String getC2CSellUrl() {
        return host + "/otc/order/sell";
    }

    public static String getMyOrderUrl() {
        return host + "/otc/order/self";
    }

    public static String getExtractinfoUrl() {
        return host + "/uc/withdraw/support/coin/info";
    }

    public static String getExtractUrl() {
        return host + "/uc/withdraw/apply";
    }

    public static String getAllTransactionUrl() {
        return host + "/uc/asset/transaction/all";
    }

    public static String getSafeSettingUrl() {
        return host + "/uc/approve/security/setting";
    }

    public static String getAvatarUrl() {
        return host + "/uc/approve/change/avatar";
    }

    public static String getBindPhoneUrl() {
        return host + "/uc/approve/bind/phone";
    }

    public static String getSendCodeUrl() {
        return host + "/uc/mobile/bind/code";
    }

    public static String getBindEmailUrl() {
        return host + "/uc/approve/bind/email";
    }

    public static String getSendEmailCodeUrl() {
        return host + "/uc/bind/email/code";
    }

    public static String getEditLoginPwdUrl() {
        return host + "/uc/mobile/update/password/code";
    }

    public static String getEditPwdUrl() {
        return host + "/uc/approve/update/password";
    }

    public static String getPlateUrl() {
        return host + "/market/exchange-plate";
    }

    public static String getEntrustUrl() {
        return host + "/exchange/order/current";
    } // 查询当前委托

    public static String getHistoryEntrus() {
        return host + "/exchange/order/history";
    } // 获取历史委托记录

    public static String getCancleEntrustUrl() {
        return host + "/exchange/order/cancel/";
    }

    public static String getPhoneForgotPwdCodeUrl() {
        return host + "/uc/mobile/reset/code";
    }

    public static String getEmailForgotPwdCodeUrl() {
        return host + "/uc/reset/email/code";
    }

    public static String getForgotPwdUrl() {
        return host + "/uc/reset/login/password";
    }

    public static String getCaptchaUrl() {
        return host + "/uc/start/captcha";
    }

    public static String googleLoginUrl() {
        return host + "/uc/google/validate";
    }


    //发送短信接口
    public static String getSendChangePhoneCodeUrl() {
        return host + "/uc/mobile/change/code";
    }

    //验证短信接口
    public static String getFindChangePhoneCodeUrl() {
        return host + "/uc/approve/change/phone/validate ";
    }

    //新手机验证码接口
    public static String getNewSendCode() {
        return host + "/uc/mobile/change/new/code ";
    }

    //绑定新手机号接口
    public static String getNewChangeSendCode() {
        return host + "/uc/approve/change/phone ";
    }


    public static String getChangePhoneUrl() {
        return host + "/uc/approve/change/phone";
    }

    public static String getMessageUrl() {
        return host + "/uc/announcement/page";
    }

    public static String getMessageDetailUrl() {
        return host + "/uc/announcement/";
    }

    public static String getRemarkUrl() {
        return host + "/uc/feedback";
    }

    public static String getAppInfoUrl() {
        return host + "/uc/ancillary/website/info";
    }

    public static String getBannersUrl() {
        return host + "/uc/ancillary/system/advertise";
    }

    public static String getOrderDetailUrl() {
        return host + "/otc/order/detail";
    }

    public static String getCancleUrl() {
        return host + "/otc/order/cancel";
    }

    public static String getpayDoneUrl() {
        return host + "/otc/order/pay";
    }

    public static String getReleaseOrderUrl() {
        return host + "/otc/order/release";
    }

    public static String getAppealUrl() {
        return host + "/otc/order/appeal";
    }

    public static String getEditAccountPwdUrl() {
        return host + "/uc/approve/update/transaction/password";
    }

    public static String getResetAccountPwdUrl() {
        return host + "/uc/approve/reset/transaction/password";
    }

    public static String getResetAccountPwdCodeUrl() {
        return host + "/uc/mobile/transaction/code";
    }

    public static String getHistoryMessageUrl() {
        return host + "/chat/getHistoryMessage";
    }


    public static String getCreditInfo() {
        return host + "/uc/approve/real/detail";
    }

    public static String getNewVision() {
        return host + "/uc/ancillary/system/app/version/0";
    }

    public static String getSymbolUrl() {
        return host + "/market/symbol";
    }

    public static String getAccountSettingUrl() {
        return host + "/uc/approve/account/setting";
    }

    public static String setAliStatusUrl() {
        return host + "/uc/approve/update/ali/status";
    }

    public static String setBankStatusUrl() {
        return host + "/uc/approve/update/bank/status";
    }

    public static String setWxStatusUrl() {
        return host + "/uc/approve/update/wechat/status";
    }


    public static String getBindBankUrl() {
        return host + "/uc/approve/bind/bank";
    }

    public static String getUpdateBankUrl() {
        return host + "/uc/approve/update/bank";
    }

    public static String getBindAliUrl() {
        return host + "/uc/approve/bind/ali";
    }

    public static String getUpdateAliUrl() {
        return host + "/uc/approve/update/ali";
    }

    public static String getBindWechatUrl() {
        return host + "/uc/approve/bind/wechat";
    }

    public static String getUpdateWechatUrl() {
        return host + "/uc/approve/update/wechat";
    }

    public static String getCheckMatchUrl() {
        return host + "/uc/asset/wallet/match-check";
    }

    public static String getStartMatchUrl() {
        return host + "/uc/asset/wallet/match";
    }

    public static String getPromotionUrl() {
        return host + "/uc/promotion/record";
    }

    public static String getPromotionRewardUrl() {
        return host + "/uc/promotion/reward/record";
    }

    public static String getGoogleCode() {
//        return host + "/uc/google/sendgoogle";
        return host + "/uc/google/generate";
    }

    //谷歌绑定短信
    public static String bindgoogleCode() {
//        return host + "/uc/google/sendgoogle";
        return host + "/uc/mobile/bind/google/code";
    }

    //谷歌解绑短信
    public static String unbindCode() {
        return host + "/uc/mobile/unbind/google/code";
    }

    //解绑谷歌
    public static String getUnBindGoogleCode() {
//        return host + "/uc/google/jcgoogle";
        return host + "/uc/google/unbind";
    }

    //绑定谷歌
    public static String getBindGoogleCode() {
//        return host + "/uc/google/googleAuth";
        return host + "/uc/google/bind";
    }

    //    修改谷歌：先验证新的是否正确
    public static String modifyvAlidate() {
        return host + "/uc/google/modify/validate";
    }

    //修改绑定谷歌
    public static String modifyvBind() {
        return host + "/uc/google/modify";
    }

    //    解绑谷歌短信接口
    public static String modifyvcode() {
        return host + "/uc/mobile/modify/google/code";
    }

    public static String getDepth() {
        return host + "/market/exchange-plate-full";
    } // 获取深度图数据

    public static String getVolume() {
        return host + "/market/latest-trade";
    } // 获取成交数据

    public static String getUploadPicFileUrl() {
        return host + "/uc/upload/local/image";
    }


    //手机号注册
    public static String getSignUpByPhone() {
        return newhost + "/user/register";
    }

    //邮箱注册
    public static String getSignUpByEmail() {
        return host + "/uc/register/email";
    }

    //登录
    public static String getLoginUrl() {
        return newhost + "/user/login";
    }

    //历史K线数据
    public static String getKDataUrl() {
        return host + "/market/history";
    }

    //获取所有币种
    public static String getAllCurrency() {
        return host + "/market/symbol-thumb";
    }

    /**
     * 首页获取所有的币种
     */
    public static String getAllCurrencys() {
        return host + "/market/overview";
    }

    /**
     * 得到信息，来设置输入小数点位数的限制
     */
    public static String getSymbolInfo() {
        return host + "/market/symbol-info";
    }


    //查询自选
    public static String getFindUrl() {
        return host + "/exchange/favor/find";
    }

    //删除自选
    public static String getDeleteUrl() {
        return host + "/exchange/favor/delete";
    }

    //添加自选
    public static String getAddUrl() {
        return host + "/exchange/favor/add";
    }

    //提交委托
    public static String getExChangeUrl() {
        return host + "/exchange/order/add";
    }

    //获取是否商家状态
    public static String getBusinessUrl() {
        return host + "/uc/approve/certified/business/status";
    }


    //挖宝首页信息
    public static String getTreasureinfo() {
        return host + "/activity/treasure/info";
    }

    //中奖信息列表
    public static String getRewardList() {
        return host + "/activity/treasure/reward/list";
    }

    //中奖信息列表
    public static String getRewardPage() {
        return host + "/activity/treasure/reward/page";
    }

    //挖宝投票列表
    public static String getVoteList() {
        return host + "/activity/treasure/vote/list";
    }

    //首页投票
    public static String getVote() {
        return host + "/activity/treasure/execute/vote";
    }

    //检测当前商品可投票数量
    public static String getCheckVote() {
        return host + "/activity/treasure/check/vote-amount";
    }

    //    commodityId：投票商品id
    //投票订单详情     打call记录
    public static String getVoteDetail() {
        return host + "/activity/treasure/vote/detail";
    }

    //   历史记录
    public static String getVoteHistory() {
        return host + "/activity/treasure/vote/history";
    }

    //    public static String getVoteHistory() {
//        return host + "/activity/treasure/join/history";
//    }
//  打call记录
    public static String getVoteCallHistory() {
        return host + "/activity/treasure/vote/detail/history";
    }


    //    挖宝商品明细：(详情)
    public static String getjoinDetial() {
        return host + "/activity/treasure/join/detail";
    }
//    参数：commodityId，挖宝商品的id


    //挖宝活动
    public static String getactivityList() {
        return host + "/activity/treasure/activity/list";
    }

    //挖宝投票
    public static String getJoin() {
        return host + "/activity/treasure/execute/join";
    }

    //挖宝算力检测
    public static String getCheckJoin() {
        return host + "/activity/treasure/check/join-amount";
    }

    //挖宝历史
    public static String getJoinHistory() {
        return host + "/activity/treasure/join/history";
    }


    //首页我的挖宝
    public static String getUserList() {
        return host + "/activity/treasure/user/list";
    }

    //我的挖宝历史
    public static String getUserHistory() {
        return host + "/activity/treasure/user/history";
    }


    //我的挖宝历史
    public static String getPowerDetail() {
        return host + "/activity/treasure/member/power/detail";
    }


    //签到
    public static String getSignin() {
        return host + "/activity/treasure/power/signin";
    }

    //修改昵称
    public static String getNikeName() {
        return host + "/uc/approve/change/username";
    }


    //    获取算力球列表：
    public static String getPowerRecord() {
        return host + "/activity/treasure/power/collect/record";
    }

    //    收取算力：
    public static String powerCollect() {
        return host + "/activity/treasure/power/collect";
    }
//    参数算力球id ：powerId


}
