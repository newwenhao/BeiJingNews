package com.beijingnews.domain;

import java.util.List;

/**
 * Created by Administrator on 2017/4/22.
 * 作用：叶签页面的数据
 */

public class TabDetailPagerBean {
    private Data data;

    private int retcode;

    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }
    public void setRetcode(int retcode){
        this.retcode = retcode;
    }
    public int getRetcode(){
        return this.retcode;
    }
    public class Data {
        private String countcommenturl;

        private String more;

        private List<News> news ;

        private String title;

        private List<Topic> topic ;

        private List<Topnews> topnews ;

        public void setCountcommenturl(String countcommenturl){
            this.countcommenturl = countcommenturl;
        }
        public String getCountcommenturl(){
            return this.countcommenturl;
        }
        public void setMore(String more){
            this.more = more;
        }
        public String getMore(){
            return this.more;
        }
        public void setNews(List<News> news){
            this.news = news;
        }
        public List<News> getNews(){
            return this.news;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setTopic(List<Topic> topic){
            this.topic = topic;
        }
        public List<Topic> getTopic(){
            return this.topic;
        }
        public void setTopnews(List<Topnews> topnews){
            this.topnews = topnews;
        }
        public List<Topnews> getTopnews(){
            return this.topnews;
        }

    }
    public class Topnews {
        private boolean comment;

        private String commentlist;

        private String commenturl;

        private int id;

        private String pubdate;

        private String title;

        private String topimage;

        private String type;

        private String url;

        public void setComment(boolean comment){
            this.comment = comment;
        }
        public boolean getComment(){
            return this.comment;
        }
        public void setCommentlist(String commentlist){
            this.commentlist = commentlist;
        }
        public String getCommentlist(){
            return this.commentlist;
        }
        public void setCommenturl(String commenturl){
            this.commenturl = commenturl;
        }
        public String getCommenturl(){
            return this.commenturl;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setPubdate(String pubdate){
            this.pubdate = pubdate;
        }
        public String getPubdate(){
            return this.pubdate;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setTopimage(String topimage){
            this.topimage = topimage;
        }
        public String getTopimage(){
            return this.topimage;
        }
        public void setType(String type){
            this.type = type;
        }
        public String getType(){
            return this.type;
        }
        public void setUrl(String url){
            this.url = url;
        }
        public String getUrl(){
            return this.url;
        }

    }
    public class News {
        private int id;

        private String title;

        private String url;

        private String listimage;

        private String pubdate;

        private boolean comment;

        private String commenturl;

        private String type;

        private String commentlist;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setUrl(String url){
            this.url = url;
        }
        public String getUrl(){
            return this.url;
        }
        public void setListimage(String listimage){
            this.listimage = listimage;
        }
        public String getListimage(){
            return this.listimage;
        }
        public void setPubdate(String pubdate){
            this.pubdate = pubdate;
        }
        public String getPubdate(){
            return this.pubdate;
        }
        public void setComment(boolean comment){
            this.comment = comment;
        }
        public boolean getComment(){
            return this.comment;
        }
        public void setCommenturl(String commenturl){
            this.commenturl = commenturl;
        }
        public String getCommenturl(){
            return this.commenturl;
        }
        public void setType(String type){
            this.type = type;
        }
        public String getType(){
            return this.type;
        }
        public void setCommentlist(String commentlist){
            this.commentlist = commentlist;
        }
        public String getCommentlist(){
            return this.commentlist;
        }

    }
    public class Topic{

    }
}