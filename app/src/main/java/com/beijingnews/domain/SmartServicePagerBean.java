package com.beijingnews.domain;

import java.util.List;

/**
 * Created by Administrator on 2017/4/27.
 */

public class SmartServicePagerBean {
    private String copyright;

    private int currentPage;

    private List<list> list ;

    private List<Orders> orders ;

    private int pageSize;

    private int totalCount;

    private int totalPage;

    public void setCopyright(String copyright){
        this.copyright = copyright;
    }
    public String getCopyright(){
        return this.copyright;
    }
    public void setCurrentPage(int currentPage){
        this.currentPage = currentPage;
    }
    public int getCurrentPage(){
        return this.currentPage;
    }
    public void setList(List<list> list){
        this.list = list;
    }
    public List<list> getList(){
        return this.list;
    }
    public void setOrders(List<Orders> orders){
        this.orders = orders;
    }
    public List<Orders> getOrders(){
        return this.orders;
    }
    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }
    public int getPageSize(){
        return this.pageSize;
    }
    public void setTotalCount(int totalCount){
        this.totalCount = totalCount;
    }
    public int getTotalCount(){
        return this.totalCount;
    }
    public void setTotalPage(int totalPage){
        this.totalPage = totalPage;
    }
    public int getTotalPage(){
        return this.totalPage;
    }

    public class list {
        private int campaignId;

        private int categoryId;

        private int id;

        private String imgUrl;

        private String name;

        private double price;

        private int sale;

        public void setCampaignId(int campaignId){
            this.campaignId = campaignId;
        }
        public int getCampaignId(){
            return this.campaignId;
        }
        public void setCategoryId(int categoryId){
            this.categoryId = categoryId;
        }
        public int getCategoryId(){
            return this.categoryId;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setImgUrl(String imgUrl){
            this.imgUrl = imgUrl;
        }
        public String getImgUrl(){
            return this.imgUrl;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setPrice(double price){
            this.price = price;
        }
        public double getPrice(){
            return this.price;
        }
        public void setSale(int sale){
            this.sale = sale;
        }
        public int getSale(){
            return this.sale;
        }

    }

    public class Orders{

    }

}