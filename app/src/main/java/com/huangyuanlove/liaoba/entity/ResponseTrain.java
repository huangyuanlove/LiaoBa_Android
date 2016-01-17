package com.huangyuanlove.liaoba.entity;

import java.util.List;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/17
 */
public class ResponseTrain {



    private int code;
    private String text;
    private List<ListEntity> list;

    public ResponseTrain() {
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

        private String trainnum;
        private String start;
        private String terminal;
        private String starttime;
        private String endtime;
        private String icon;
        private String detailurl;

        public void setTrainnum(String trainnum) {
            this.trainnum = trainnum;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }

        public String getTrainnum() {
            return trainnum;
        }

        public String getStart() {
            return start;
        }

        public String getTerminal() {
            return terminal;
        }

        public String getStarttime() {
            return starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public String getIcon() {
            return icon;
        }

        public String getDetailurl() {
            return detailurl;
        }
    }
}
