package com.huangyuanlove.liaoba.entity;

import java.util.List;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/17
 */
public class ResponseNews {




    private int code;
    private String text;
    private List<ListEntity> list;

    public ResponseNews() {
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        /**
         * article :
         * source :
         * detailurl :
         * icon :
         */

        private String article;
        private String source;
        private String detailurl;
        private String icon;

        public void setArticle(String article) {
            this.article = article;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getArticle() {
            return article;
        }

        public String getSource() {
            return source;
        }

        public String getDetailurl() {
            return detailurl;
        }

        public String getIcon() {
            return icon;
        }
    }
}
