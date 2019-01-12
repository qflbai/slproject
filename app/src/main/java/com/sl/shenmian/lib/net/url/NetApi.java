package com.sl.shenmian.lib.net.url;

/**
 * @author WenXian Bai
 * @Date: 2018/3/14.
 * @Description: 网络请求接口 （建议所有网络请求接口地址统一写在这里，便于管理）
 */

public final class NetApi {
    private NetApi() {
    }

    /**
     * 上下文
     */
    private static String mToken;

    /**
     * 设置token
     *
     * @param token
     */
    public static void setToken(String token) {
        mToken = token;
    }

    public static String getToken() {
        return mToken;
    }


    public static class App {
        /**
         * 更新版本
         */
        public static final String UPDATA_VERSION="plugins/updateAppVersion";
        /**
         * 手机兼容接口
         */
        public static final String PHONE_COMPATIBILITY_CHECK= "plugins/incompatibleModel/check";

        /**
         * 施封接口
         */
        public static final String PADLOCK_INFO = "mvc/appindex/sealing/saveSealingInfo.mvc";

        /**
         * 封条状态
         */
        public static final String SEAL_STATUS = "mvc/appindex/sealing/getSealSatus.mvc";

    }
}
