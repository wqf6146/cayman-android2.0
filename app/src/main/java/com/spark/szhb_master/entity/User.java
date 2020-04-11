package com.spark.szhb_master.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/26.
 */

public class User extends DataSupport implements Serializable {
    private String token;
    private String email;
    private int is_pwd;         //是否设置交易密码：0未设置1已设置
    private Location location;
    private int memberLevel;
    private String nick_name;   //用户昵称
    private String real_name;   //用户真实姓名
    private String phone;
    private int shop_type;   //是否是商家：0否 1是
    private int status;      //账号状态0正常1异常
    private int type;           //身份认证： 0 未认证 1 待审核 2 已通过 3拒绝
    private boolean isSelect;
    private Country country;
    private String avatar;
    private int id;
    private int is_start_google;

    public int getIs_pwd() {
        return is_pwd;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getShop_type() {
        return shop_type;
    }

    public int getStatus() {
        return status;
    }

    public Location getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public String getNick_name() {
        return nick_name;
    }

    public int getType() {
        return type;
    }

    public String getPhone() {
        return phone;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setIs_pwd(int is_pwd) {
        this.is_pwd = is_pwd;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setMemberLevel(int memberLevel) {
        this.memberLevel = memberLevel;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public void setShop_type(int shop_type) {
        this.shop_type = shop_type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIs_start_google() {
        return is_start_google;
    }

    public void setIs_start_google(int is_start_google) {
        this.is_start_google = is_start_google;
    }

    private String promotionPrefix;
    private String promotionCode;

    public String getPromotionPrefix() {
        return promotionPrefix;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public int getMemberLevel() {
        return memberLevel;
    }


    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", nikename='" + nick_name + '\'' +
                ", location=" + location +
                ", memberLevel=" + memberLevel +
                ", realName='" + real_name + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }

    public static class Location implements Serializable {

        private String country;
        private String province;
        private String city;
        private String district;


        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

    }

}
