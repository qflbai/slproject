package com.sl.shenmian.lib.net.download;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


import com.alibaba.fastjson.JSON;
import com.sl.shenmian.lib.net.RetrofitManage;
import com.sl.shenmian.lib.net.assist.DownloadFileAssist;
import com.sl.shenmian.lib.net.body.DownloadInfo;
import com.sl.shenmian.lib.net.download.modle.CheckVersoinInfo;
import com.sl.shenmian.lib.net.download.modle.VersionUpdataBean;
import com.sl.shenmian.lib.net.listener.DownloadListener;
import com.sl.shenmian.lib.net.retrofit.RetrofitService;
import com.sl.shenmian.lib.net.url.NetApi;
import com.sl.shenmian.lib.utils.SystemUtil;
import com.sl.shenmian.lib.utils.toast.ToastUtil;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @author WenXian Bai
 * @Date: 2018/3/29.
 * @Description: 下载APP工具
 */

public class DownloadAppUtil {
    private final String TAG = DownloadAppUtil.class.getSimpleName();
    private AppCompatActivity mActivity;
    private Disposable mSubscribe;

    /**
     * 检测版本号
     */
    public void checkVersion(AppCompatActivity activity) {
        mActivity = null;
        mActivity = activity;
        CheckVersoinInfo checkVersoinInfo = new CheckVersoinInfo();
        checkVersoinInfo.setAppid("0");
        RetrofitManage retrofitManage = new RetrofitManage();
        RetrofitService service = retrofitManage.createService();
        String pathUrl = NetApi.App.UPDATA_VERSION;
        mSubscribe = service.postJsonNet(pathUrl, checkVersoinInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    /**
                     * Consume the given value.
                     *
                     * @param response the value
                     * @throws Exception on error
                     */
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
                        try {
                            String json = response.body().string();
                            //Log.e(TAG, json);
                            VersionUpdataBean versionUpdataBean = JSON.parseObject(json, VersionUpdataBean.class);

                            if (versionUpdataBean == null) {
                                return;
                            }

                            String versionString = versionUpdataBean.getVersion();
                            String localVersionName = SystemUtil.getVersionName(mActivity.getApplicationContext());

                            boolean checkVersion = checkVersion(versionString, localVersionName);

                            if (checkVersion) {
                                String url = versionUpdataBean.getUrl();
                                String fdetail = versionUpdataBean.getDesc();
                                String forcedupdate = versionUpdataBean.getForcedupdate();
                                updataWindow(mActivity, url, versionString, fdetail,forcedupdate);
                            } else {
                                ToastUtil.show(mActivity, "当前已是最新版本");
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * <oo_oo></oo_oo>
     * 比较版本大小
     *
     * @param serverVersion
     * @param clientVersion
     * @return
     */
    public static boolean checkVersion(String serverVersion, String clientVersion) {

        boolean bool = false;
        String sV = serverVersion;
        String cV = clientVersion;

        String[] sVs = null;
        String[] cVs = null;

        if (sV.length() > 0 && sV.indexOf(".") != -1) {
            sVs = sV.split("\\.");
        }

        if (cV.length() > 0 && cV.indexOf(".") != -1) {
            cVs = cV.split("\\.");
        }

        if (null != sVs && sVs.length > 0 && null != cVs && cVs.length > 0) {

            for (int i = 0; i < sVs.length; i++) {
                String s = sVs[i];
                String c = cVs[i];

                if (null != s && null != c && s.length() > 0 && c.length() > 0) {

                    int sint = Integer.parseInt(s);
                    int cint = Integer.parseInt(c);
                    if (sint > cint) {
                        bool = true;
                        break;
                    } else if (sint < cint) {
                        bool = false;
                        break;
                    }
                }
            }
        }
        return bool;
    }


    /**
     * 更新弹窗
     */
    private void updataWindow(AppCompatActivity activity, final String url, String newVersionName, final String updataMes, final String forcedupdate) {
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本" + ":" + SystemUtil.getVersionName(activity));
        sb.append("\n新版本:" + newVersionName);
        sb.append("\n" + updataMes);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("版本更新")
                .setMessage(sb.toString())
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        downLoadApk(url, forcedupdate);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    /**
     * 下载apk
     *
     * @param url
     */
    private void downLoadApk(String url,final String forcedupdate) {

        String savnParentPath = Environment.getExternalStorageDirectory().getPath() + "/suntech/lzwc";
        File parentFile = new File(savnParentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String savnPath = savnParentPath + File.separator + "lzwc.apk";
        final DownloadFileAssist downloadFileAssist = new DownloadFileAssist();
        downloadFileAssist.downloadFile(url, savnPath, new DownloadListener() {
            @Override
            public void onStarted() {

                pd = new ProgressDialog(mActivity);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setTitle("apk下载");
                pd.setMessage("正在下载中");
                pd.setMax((int) 100);
                pd.setCanceledOnTouchOutside(false);
                if(null != forcedupdate && forcedupdate.equals("1")) {
                    pd.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downloadFileAssist.cancalDown();
                        }
                    });
                }
                pd.show();
            }

            @Override
            public void onSuccess(File result) {
                pd.dismiss();
                installApk(result);
            }

            @Override
            public void onLoading(DownloadInfo downloadInfo) {
                pd.setProgress((int) downloadInfo.getProgress());
            }

            @Override
            public void onFail(Throwable ex) {
                mActivity = null;
                pd.dismiss();
            }
        });

    }


    static ProgressDialog pd = null;

    private static final int ENTERHOME = 6;

    /**
     * 提示用户安装apk
     *
     * @param file 隐式意图安装
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    mActivity.getApplicationContext(),
                    mActivity.getPackageName() + ".fileprovider",
                    file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        mActivity.startActivity(intent);
    }
}
