package com.spark.szhb_master.entity;

public class DepthListInfo {
        private double price;
        private double amount;

        public DepthListInfo(){
        }

        public DepthListInfo(double price,double amount){
            this.price = price;
            this.amount = amount;
        }
        
        public double getPrice() {
            return price;
        }

        public double getAmount() {
            return amount;
        }


        public void setPrice(double price) {
            this.price = price;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }