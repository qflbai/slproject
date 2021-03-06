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
    private int uploadingState;
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
    /**
     * 地址id
     */
    @ColumnInfo
    private String addressId;
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
    @ColumnInfo
    private String imagePath1;
    @ColumnInfo
    private String imagePath2;
    @ColumnInfo
    private String imagePath3;
    @ColumnInfo
    private String carLicense;
    /**
     * 车牌id
     */
    @ColumnInfo
    private int carLicenseId;
    @ColumnInfo
    private String lockedImei;
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

    public int getUploadingState() {
        return uploadingState;
    }

    public void setUploadingState(int uploadingState) {
        this.uploadingState = uploadingState;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getImagePath3() {
        return imagePath3;
    }

    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }

    public String getLockedImei() {
        return lockedImei;
    }

    public void setLockedImei(String lockedImei) {
        this.lockedImei = lockedImei;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public int getCarLicenseId() {
        return carLicenseId;
    }

    public void setCarLicenseId(int carLicenseId) {
        this.carLicenseId = carLicenseId;
    }
}
