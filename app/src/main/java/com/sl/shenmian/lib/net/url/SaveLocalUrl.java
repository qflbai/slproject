package com.sl.shenmian.lib.net.url;

import android.content.Context;

import com.sl.shenmian.lib.constant.ConstantValues;
import com.sl.shenmian.lib.utils.sharedpreferences.SpUtil;

/**
 * @author WenXian Bai
 * @Date: 2018/12/6.
 * @Description:
 */
public class SaveLocalUrl {
    /**
     * 保存本地im ip
     *
     * @param context
     * @param baseUrl
     */
    public static void saveLocatIMUrl(Context context, String baseUrl) {
        String tag = "/";
        String substring = baseUrl.substring(baseUrl.length() - 1, baseUrl.length());
        if (!substring.equals(tag)) {
            baseUrl = baseUrl + tag;
        }
        SpUtil.putString(context, ConstantValues.SP.KEY_UTL_IM_IP, baseUrl);
    }

    /**
     * 保存本地im ip
     *
     * @param context
     * @param baseUrl
     */
    public static void saveLocatScanUrl(Context context, String baseUrl) {
        String tag = "/";
        String substring = baseUrl.substring(baseUrl.length() - 1, baseUrl.length());
        if (!substring.equals(tag)) {
            baseUrl = baseUrl + tag;
        }
        SpUtil.putString(context, ConstantValues.SP.KEY_URL_SCAN_IP, baseUrl);
    }

    /**
     * 清空本地
     */
    public static void clearLocalUrl(Context context){
        SpUtil.putString(context, ConstantValues.SP.KEY_UTL_IM_IP, "");
        SpUtil.putString(context, ConstantValues.SP.KEY_URL_SCAN_IP, "");
    }

    /**
     * 获取本地保存地址
     */
    public static void initLocatUrl(Context context) {
        String scanIp = SpUtil.getString(context, ConstantValues.SP.KEY_URL_SCAN_IP, "");
        String imIp = SpUtil.getString(context, ConstantValues.SP.KEY_UTL_IM_IP, "");
        if (!scanIp.isEmpty()) {
            NetBaseUrl.setBaseURL(scanIp);
        }

        if (!imIp.isEmpty()) {
            NetBaseUrl.setIMBaseUrl(imIp);
        }
    }

    /**
     * 获取本地IM ip
     * @param context
     * @return
     */
    public static String getLocatImIp(Context context){
        String imIp = SpUtil.getString(context, ConstantValues.SP.KEY_UTL_IM_IP, "");
        return imIp;
    }

    /**
     * 获取本地scan ip
     * @param context
     * @return
     */
    public static String getLocatScanIp(Context context){
        String imIp = SpUtil.getString(context, ConstantValues.SP.KEY_URL_SCAN_IP, "");
        return imIp;
    }
}
