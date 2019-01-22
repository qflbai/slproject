package com.sl.shenmian.module.offline.model;



/**
 * @author WenXian Bai
 * @Date: 2018/9/25.
 * @Description:
 */
public class OfflineInfo {
    private long id ;
    private String coding;
    private String address;
    private String addressId;
    private String time;
    private int uploadingStae;
    private String remark;
    private String carLicense;
    private String lockedImei;
    private String userAccount;
    private String imagePath1;
    private String imagePath2;
    private String imagePath3;
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

    public int getUploadingStae() {
        return uploadingStae;
    }

    public void setUploadingStae(int uploadingStae) {
        this.uploadingStae = uploadingStae;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}