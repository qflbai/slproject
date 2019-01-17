package com.sl.shenmian.lib.net.url;

import com.sl.shenmian.BuildConfig;

/**
 * @author WenXian Bai
 * @Date: 2018/3/14.
 * @Description: 网络请求服务器地址
 * url格式：protocol :// hostname[:port] / path /
 */

public class NetBaseUrl {
    /**
     * http协议
     */
    private static final String URL_HTTP = "http://";
    /**
     * 端口号
     */
    private static final String URL_PORT = ":80";
    /**
     * https协议
     */
    private static final String URL_HTTPS = "https://";
    /**
     * 协议
     */
    public static final String URL_PROTOCOL = URL_HTTP;

    /**
     * 文件服务器ip
     */
    private static final String FILE_SERVE_IP = "http://t.sun-tech.cn:8011/";

    /**
     * 扫码平台服务器ip
     */
    private static final String scan_server_ip = "http://s.sun-tech.cn/app/";
    /**
     * 测试地址http://47.106.157.174:8088/SM_ERP
     */
    //private static final String clb_ip = "http://192.168.1.119:8081/SM_ERP/";
    private static final String clb_ip = "http://47.106.157.174:8088/sm/";
    /**
     * IM服务器地址
     */
    private static final String IM_SERVER_IP = "http://im.sun-tech.cn/";

    /**
     * 非默认ip(文件服务ip)
     */
    private static final String NO_DEFAULT_IP_FILE = FILE_SERVE_IP;

    /**
     * app release模式默认ip
     */
    private static final String URL_BASE_PATH = scan_server_ip;

    /**
     * debug模式默认ip
     */
    private static final String DEBUG_URL_IP = clb_ip;


    /**
     * release模式默认路径
     */
    private static String BASE_URL = DEBUG_URL_IP;

    /**
     * release默认IMip
     */
    private static String IM_BASE_URL = IM_SERVER_IP;

    /**
     * debug模式默认路径
     */
    private static String DEBUG_BASE_URL = DEBUG_URL_IP;

    /**
     * 获取默认请求地址
     *
     * @return
     */
    public static String getBaseUrl() {
        return BuildConfig.DEBUG ? DEBUG_BASE_URL : BASE_URL;
        // return BASE_URL;
    }

    /**
     * 获取文件服务ip
     *
     * @return
     */
    public static String getNoDefaultIpFile() {
        return NO_DEFAULT_IP_FILE;
    }

    /**
     * 获取IM服务器IP
     *
     * @return
     */
    public static String getImServerIp() {
        return IM_BASE_URL;
    }

    /**
     * 设置网络请求地址
     */
    public static void setBaseURL(String baseUrl) {
        String tag = "/";
        String substring = baseUrl.substring(baseUrl.length() - 1, baseUrl.length());
        if (!substring.equals(tag)) {
            baseUrl = baseUrl + tag;
        }
        BASE_URL = baseUrl;
        DEBUG_BASE_URL = baseUrl;
    }

    /**
     * 设置Im服务
     *
     * @param baseUrl
     */
    public static void setIMBaseUrl(String baseUrl) {
        String tag = "/";
        String substring = baseUrl.substring(baseUrl.length() - 1, baseUrl.length());
        if (!substring.equals(tag)) {
            baseUrl = baseUrl + tag;
        }
        IM_BASE_URL = baseUrl;
    }

}
