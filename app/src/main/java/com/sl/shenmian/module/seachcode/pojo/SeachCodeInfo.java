package com.sl.shenmian.module.seachcode.pojo;


import java.util.List;

public class SeachCodeInfo{

    /**
     * username : 张三
     * addr : 黄冈口岸
     * time : 2019-01-01
     * lic : 粤B 3591
     * remark : 黄金千两
     * img : [{"url":"原图链接","thumbUrl":"缩略图"},{"url":"原图链接","thumbUrl":"缩略图"},{"url":"原图链接","thumbUrl":"缩略图"}]
     */

    private String username;
    private String addr;
    private String time;
    private String lic;
    private String remark;
    private List<ImgBean> img;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLic() {
        return lic;
    }

    public void setLic(String lic) {
        this.lic = lic;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ImgBean> getImg() {
        return img;
    }

    public void setImg(List<ImgBean> img) {
        this.img = img;
    }

    public static class ImgBean {
        /**
         * url : 原图链接
         * thumbUrl : 缩略图
         */

        private String url;
        private String thumbUrl;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }
    }
}
