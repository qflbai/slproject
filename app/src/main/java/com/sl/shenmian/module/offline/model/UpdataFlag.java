package com.sl.shenmian.module.offline.model;

/**
 * @author WenXian Bai
 * @Date: 2018/9/26.
 * @Description:
 */
public class UpdataFlag {
    private OfflineInfo offlineInfo;
    private int position;

    public OfflineInfo getOfflineInfo() {
        return offlineInfo;
    }

    public void setOfflineInfo(OfflineInfo offlineInfo) {
        this.offlineInfo = offlineInfo;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
