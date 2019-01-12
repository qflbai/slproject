package com.sl.shenmian.module.seachcode.pojo;

import java.io.Serializable;

public class Image implements Serializable {
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
