package com.sl.shenmian.lib.constant;

/**
 * @author WenXian Bai
 * @Date: 2018/3/26.
 * @Description: 常量值(用于sp key 和 value)
 */

public class ConstantValues {
    /**
     * 引导页是否第一次打开key
     */
    public static final String KEY_GUIDE_ACTIVITY_IS_FIRST_OPEN = "key_guide_activity_is_first_open";

    /**
     * 用户信息
     */
    public static class UserInfo {
        /**
         * 用户信息key
         */
        public static final String KEY_USER_ACCOUNT = "key_user_account";
    }

    public static class OfflineInfo{
        public static final String KEY_SEAL_TYPE = "key_seal_type";
    }

    public static class RelevanInfo {
        public static final String KEY_UNIT_NUM = "key_unit_num";
        public static final String DEFAULT_VALUE_UNIT_NUM = "50";
        /**
         * 关联模板key
         */
        public static final String KEY_TEMPLATE_INFOS = "key_template_infos";
        /**
         * 关联模板信息列表key
         */
        public static final String KEY_TEMPLATE_INFO = "key_template_info";

    }

    public static class Traceability {
        /**
         * 溯源信息key
         */
        public static final String KEY_TRACEABILITY_INFO = "key_traceability_info";
        /**
         * 码值
         */
        public static final String KEY_CODE = "key_code";
    }

    /**
     * 意图action
     */
    public static class Action {
        /**
         * 主页 //todo :清单文件必须设，否则报错*/

    }

    public static class Activity {
        /**
         * activity
         */
        public static final String KEY_ACTIVITY = "key_activity";

    }

    /**
     * 事件常量
     */
    public static class Event {
        /**
         * 定位key
         */
        public static final String KEY_LOCATION = "key_location";
        /**
         * sdk 信息key
         */
        public static final String KEY_SDK_INFO = "key_sdk_info";
        /**
         * 扫码结果信息web页面key
         */
        public static final String KEY_ACTIVITY_WEB_SCAN_INFO="key_activity_web_scan_info";
        /**
         * 蓝牙扫码结果信息web页面key
         */
        public static final String KEY_ACTIVITY_WEB_BLUETOOTH_SCAN_INFO="key_activity_web_bluetooth_scan_info";
        /**
         * web结果信息web页面key
         */
        public static final String KEY_ACTIVITY_WEB_INFO="key_activity_web_info";
        /**
         * 网络信息key
         */
        public static final String KEY_NET_INFO="key_net_info";
        /**
         * 登录信息key
         */
        public static final String KEY_LOGIN_USER_INFO = "key_login_user_info";
        /**
         * 变更扫码模式key
         */
        public static final String KEY_CHANGE_SCAN_MODE = "key_change_scan_mode";
    }

    /**
     * 本地保存key
     */
    public static class SP{
        /**
         * 扫码ipkey
         */
        public static final String KEY_URL_SCAN_IP ="key_url_scan_ip";
        /**
         * im ip key
         */
        public static final String KEY_UTL_IM_IP = "key_utl_im_ip";
    }
}
