package com.spark.szhb_master.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class AccountSetting implements Serializable {
    private String realName;
    private int bankVerified;
    private int aliVerified;
    private int wechatVerified;
    private BankInfoBean bankInfo;
    private AlipayBean alipay;
    private WeChatBean wechatPay;

    public String getRealName() {
        return realName;
    }

    public int getBankVerified() {
        return bankVerified;
    }

    public int getAliVerified() {
        return aliVerified;
    }

    public int getWechatVerified() {
        return wechatVerified;
    }

    public BankInfoBean getBankInfo() {
        return bankInfo;
    }

    public AlipayBean getAlipay() {
        return alipay;
    }

    public WeChatBean getWechatPay() {
        return wechatPay;
    }


    public static class BankInfoBean implements Serializable {
        /**
         * bank : 中国工商银行
         * branch : 4156453123132
         * cardNo : 123456789632145678
         */

        private String bank;
        private String branch;
        private String cardNo;
        private int bankStatus;

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBranch() {
            return branch;
        }

        public String getCardNo() {
            return cardNo;
        }

        public int getBankStatus() {
            return bankStatus;
        }

        public void setBankStatus(int bankStatus) {
            this.bankStatus = bankStatus;
        }
    }

    public static class AlipayBean implements Serializable {
        /**
         * aliNo : 18255478978
         * qrCodeUrl : null
         */

        private String aliNo;
        private String qrCodeUrl;
        private int aliStatus;

        public int getAliStatus() {
            return aliStatus;
        }

        public void setAliStatus(int aliStatus) {
            this.aliStatus = aliStatus;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }

        public String getAliNo() {
            return aliNo;
        }

    }

    public static class WeChatBean implements Serializable {
        /**
         * aliNo : 18255478978
         * qrCodeUrl : null
         */

        private String wechat;
//        private String qrCodeUrl;
        private String qrWeCodeUrl;
        private int wechatStatus;

        public int getWechatStatus() {
            return wechatStatus;
        }

        public void setWechatStatus(int wechatStatus) {
            this.wechatStatus = wechatStatus;
        }

        public String getweChat() {
            return wechat;
        }

        public String getQrWeCodeUrl() {
            return qrWeCodeUrl;
        }

        public void setQrWeCodeUrl(String qrWeCodeUrl) {
            this.qrWeCodeUrl = qrWeCodeUrl;
        }
    }

}
