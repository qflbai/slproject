package com.sl.shenmian.module.seachcode.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SeachCodeInfo implements Serializable{
    private String username;
    private String addr;
    private String time;
    private String lic;
    private String remark;
    private List<Image> siglist = new ArrayList<>();

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

    public List<Image> getSiglist() {
        return siglist;
    }

    public void setSiglist(List<Image> siglist) {
        this.siglist = siglist;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
