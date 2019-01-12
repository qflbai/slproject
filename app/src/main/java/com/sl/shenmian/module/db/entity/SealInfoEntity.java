package com.sl.shenmian.module.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @Description: java类作用描述
 * @Author: qflbai
 * @CreateDate: 2019/1/12 13:54
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/12 13:54
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Entity
public class SealInfoEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    /**
     * 账号
     */
    @ColumnInfo
    private String userAccount;
    /**
     * 施封人
     */
    @ColumnInfo
    private String userName;
    /**
     * 封条类型
     */
    @ColumnInfo
    private int sealType;
    /**
     * 是否上传( 0:未上传 1:上传)
     */
    @ColumnInfo
    private int uploadingStae;
    /**
     * 封条状态
     */
    @ColumnInfo
    private String sealState;
    /**
     * 编码
     */
    @ColumnInfo
    private String coding;
    /**
     * 地址
     */
    @ColumnInfo
    private String address;
    @ColumnInfo
    /**
     * 时间
     */
    private String time;
    /**
     * 备注
     */
    @ColumnInfo
    private String remark;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSealType() {
        return sealType;
    }

    public void setSealType(int sealType) {
        this.sealType = sealType;
    }

    public String getSealState() {
        return sealState;
    }

    public void setSealState(String sealState) {
        this.sealState = sealState;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getUploadingStae() {
        return uploadingStae;
    }

    public void setUploadingStae(int uploadingStae) {
        this.uploadingStae = uploadingStae;
    }
}
