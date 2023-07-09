package com.aib.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class HomeBean implements MultiItemEntity {
    private ProductBean product;
    private List<BannerBean> banner;

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public static final int BANNER = 1;
    public static final int PRODUCT = 2;
    private int itemType;

    public HomeBean(ProductBean product, List<BannerBean> banner, int itemType) {
        this.product = product;
        this.banner = banner;
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public static class ProductBean {
        /**
         * id : 3
         * name : 月月升理财计划
         * money : 200
         * yearRate : 11.00
         * suodingDays : 100
         * minTouMoney : 10000
         * menberNum : 400
         * progress : 90
         */

        private int id;
        private String name;
        private int money;
        private String yearRate;
        private int suodingDays;
        private int minTouMoney;
        private int menberNum;
        private int progress;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getYearRate() {
            return yearRate;
        }

        public void setYearRate(String yearRate) {
            this.yearRate = yearRate;
        }

        public int getSuodingDays() {
            return suodingDays;
        }

        public void setSuodingDays(int suodingDays) {
            this.suodingDays = suodingDays;
        }

        public int getMinTouMoney() {
            return minTouMoney;
        }

        public void setMinTouMoney(int minTouMoney) {
            this.minTouMoney = minTouMoney;
        }

        public int getMenberNum() {
            return menberNum;
        }

        public void setMenberNum(int menberNum) {
            this.menberNum = menberNum;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }
    }

    public static class BannerBean {
        /**
         * id : 1
         * img : img/banner/index01.png
         * url : https://github.com/hisgod
         */

        private int id;
        private String img;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

