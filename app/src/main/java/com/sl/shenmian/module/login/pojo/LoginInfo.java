package com.sl.shenmian.module.login.pojo;

/**
 * @Description: java类作用描述
 * @Author: qflbai
 * @CreateDate: 2019/1/12 18:03
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/12 18:03
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LoginInfo {

    /**
     * success : true
     * resultCode : 0
     * message : 登录成功！
     * data : {"id":"8","sex":"MAN","username":"","token":"039438427C30880F73D9FB598BA3585C","img":"man.png","loginCode":"Effendi"}
     * timestamp : 1547113116710
     */

    private boolean success;
    private String resultCode;
    private String message;
    private DataBean data;
    private long timestamp;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataBean {
        /**
         * id : 8
         * sex : MAN
         * username :
         * token : 039438427C30880F73D9FB598BA3585C
         * img : man.png
         * loginCode : Effendi
         */

        private String id;
        private String sex;
        private String username;
        private String token;
        private String img;
        private String loginCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getLoginCode() {
            return loginCode;
        }

        public void setLoginCode(String loginCode) {
            this.loginCode = loginCode;
        }
    }
}
