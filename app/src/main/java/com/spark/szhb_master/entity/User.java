package com.spark.szhb_master.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/26.
 */

public class User extends DataSupport implements Serializable {
    private String token;
    private String username;
    private Location location;
    private int memberLevel;
    private String realName;
    private boolean isSelect;
    private Country country;
    private String avatar;
    private int id;
    private int googleState;

    public int getGoogleState() {
        return googleState;
    }

    public void setGoogleState(int googleState) {
        this.googleState = googleState;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
                ", username='" + username + '\'' +
                ", location=" + location +
                ", memberLevel=" + memberLevel +
                ", realName='" + realName + '\'' +
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
