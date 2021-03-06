package com.sl.shenmian.lib.net.state;

/**
 * @author WenXian Bai
 * @Date: 2018/5/15.
 * @Description:服务器响应状态值
 */
public class ServerResponseState {
    /**
     * 成功
     */
    public static final String ST_0 = "0";
    public static final String ST_011001 = "SL000001";
    public static final String ST_011002 = "SL000002";
    public static final String ST_011003 = "SL000003";
    public static final String ST_011004 = "SL000004";
    public static final String ST_011005 = "PO000001";
    public static final String ST_011006 = "MU000001";
    public static final String ST_011007 = "ST011007";
    public static final String ST_011099 = "ST011099";

    public static String getStateMessage(String state) {
        String message = "";
        switch (state) {
            case ST_0:
                message = "登陆成功";
                break;
            case ST_011001:
                message = "账号不存在";
                break;
            case ST_011002:
                message = "密码错误";
                break;
            case ST_011003:
                message = "当前设备未授权";
                break;
            case ST_011004:
                message = ":设备已过有效日期";
                break;
            case ST_011005:
                message = "缺少必要参数";
                break;
            case ST_011006:
                message = "密码错误";
                break;
            case ST_011007:
                message = "权限不足";
                break;
            default:
                break;
        }
        return message;
    }
}
