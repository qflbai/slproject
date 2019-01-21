package com.sl.shenmian.module.login.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.AbsActivity;
import com.sl.shenmian.lib.constant.ConstantValues;
import com.sl.shenmian.lib.net.RetrofitManage;
import com.sl.shenmian.lib.net.body.ServerResponseResult;
import com.sl.shenmian.lib.net.callback.DataNetCallback;
import com.sl.shenmian.lib.net.callback.NetCallback;
import com.sl.shenmian.lib.net.retrofit.RetrofitService;
import com.sl.shenmian.lib.net.rxjava.DataNetObserver;
import com.sl.shenmian.lib.net.rxjava.NetObserver;
import com.sl.shenmian.lib.net.state.ServerResponseState;
import com.sl.shenmian.lib.net.url.NetApi;
import com.sl.shenmian.lib.net.url.NetBaseUrl;
import com.sl.shenmian.lib.ui.dialog.CustomDialog;
import com.sl.shenmian.lib.ui.dialog.LoadingDialog;
import com.sl.shenmian.lib.utils.SystemUtil;
import com.sl.shenmian.lib.utils.sharedpreferences.SpUtil;
import com.sl.shenmian.lib.utils.toast.ToastUtil;
import com.sl.shenmian.module.login.pojo.LoginInfo;
import com.sl.shenmian.module.login.pojo.UserInfo;
import com.sl.shenmian.module.login.viewmodel.LoginViewModel;
import com.sl.shenmian.module.main.ui.MainActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginActivity extends AbsActivity<LoginViewModel> {

    @BindView(R.id.editText2)
    EditText account_ed;
    @BindView(R.id.editText3)
    EditText password_ed;
    @BindView(R.id.checkBox)
    CheckBox re_password_ck;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.app_icon)
    ImageView app_icon;
    @BindView(R.id.version_tv)
    TextView version_tv;

    //记住密码
    private boolean isRePASSWORD = true;

    //相机权限 手机权限 定位权限gps 网络 存储权限
    private String[] mPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    private boolean mIsHaveAllPermissions = false;
    private NetObserver dataNetObserver;
    private boolean isRememberPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_login);
        initCheckPermissions();
        initUI();
        loadLoginInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }

    private void initUI() {
        isRememberPwd = SpUtil.getBoolean(mContext, ConstantValues.UserInfo.key_remember_pwd, false);
        re_password_ck.setChecked(isRememberPwd);
        isRePASSWORD = re_password_ck.isChecked();
        re_password_ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtil.putBoolean(mContext, ConstantValues.UserInfo.key_remember_pwd, isChecked);
            }
        });
        app_icon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                OnLongClick(v);
                return false;
            }
        });

        version_tv.setText("v"+SystemUtil.getVersionName(this));
    }

    private void initCheckPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissionAllGranted(mPermissions)) {
                mIsHaveAllPermissions = false;
                ActivityCompat.requestPermissions(this, mPermissions, 201);
                return;
            } else {
                mIsHaveAllPermissions = true;
                //initAppData();
            }
        } else {
            mIsHaveAllPermissions = true;
            // initAppData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }


    @Override
    protected void dataObserver() {
        super.dataObserver();
    }

    @OnClick({R.id.button})
    void onClick(View v) {

        switch (v.getId()) {
            case R.id.button:
                if(SystemUtil.isNetOk(LoginActivity.this)) {
                    if (checkUserInfo()) {
                        login();
                    }
                }else {
                    showExitDialog();
                }
                break;
        }
    }

    private CustomDialog customDialog = null;
    private void showExitDialog() {
        customDialog = null;
        if (null == customDialog) {
            customDialog = new CustomDialog();
        }
        customDialog.removeWindowTitle(true);
        customDialog.setContentIconIsShow(true);
        customDialog.setRightBtnIsShow(true);
        customDialog.setDialogContentIcon(R.mipmap.fail);
        customDialog.setDialogContentMsg(R.string.net_fail_login_fail_msg);
        customDialog.setDialogLeftBtnText(R.string.ok);
        customDialog.setDialogRightBtnText(R.string.offline_login);
        customDialog.setDialogTitleBtnOnClick(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        customDialog.setDialogLeftBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        customDialog.setDialogRightBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offlineLogin();
                dismiss();
            }
        });
        customDialog.show(getSupportFragmentManager(), "mainExitDialog");
    }

    private void dismiss() {
        if (null != customDialog) {
            customDialog.dismiss();
        }
    }

    private void offlineLogin(){
        saveLoginInfo(null);
        Intent intent = new Intent(mContext, MainActivity.class);
        activityJumps(intent);
    }

    private boolean checkUserInfo() {
        String account = account_ed.getText().toString().trim();
        String password = password_ed.getText().toString().trim();
        String acc_acc_parent = "/^[A-Za-z0-9]{3,10}$/";
        String acc_pwd_parent = "/^[A-Za-z0-9]{3,14}$/";

        boolean checked = false;
        /*if (account.length() == 0) {
            if (acc_acc_parent.matches(acc_acc_parent)) {
                checked = true;
            } else {
                ToastUtil.showCenter(LoginActivity.this, getString(R.string.login_user_account_error_regex));
            }
        } else {
            ToastUtil.showCenter(LoginActivity.this, getString(R.string.login_user_account_error_empty));
        }*/
        /*if (password.length() != 0) {
            if (acc_acc_parent.matches(acc_pwd_parent)) {
                checked = true;
            } else {
                ToastUtil.showCenter(LoginActivity.this, getString(R.string.login_user_password_error_regex));
            }
        } else {
            ToastUtil.showCenter(LoginActivity.this, getString(R.string.login_user_password_error_empty));
        }*/

        if (account.isEmpty() || password.isEmpty()) {
            ToastUtil.showCenter(mContext, "账号或密码不能为空");
            return false;
        } else {
            return true;
        }
    }

    private RetrofitManage retrofitManage = null;

    private void login() {
        if(null == retrofitManage) {
            retrofitManage = new RetrofitManage();
        }
       // Log.e(TAG, "BASEURL:"+NetBaseUrl.getBaseUrl());
        FragmentManager supportFragmentManager = LoginActivity.this.getSupportFragmentManager();
        // 正在登陆
        LoadingDialog.newInstance().showLoad(supportFragmentManager, "正在登录...");

        String account = account_ed.getText().toString().trim();
        String password = password_ed.getText().toString().trim();
        HashMap<String, Object> paraMap = new HashMap<>();
        paraMap.put("loginCode", account);
        paraMap.put("password", password);
        paraMap.put("imei", SystemUtil.getImei(mContext));
        if (dataNetObserver == null) {
            dataNetObserver = new NetObserver(mContext, new NetCallback() {

                @Override
                public void onResponse(String dataJson) {
                    LoadingDialog.newInstance().dismiss();
                    LoginInfo loginInfo = JSON.parseObject(dataJson, LoginInfo.class);
                    String resultCode = loginInfo.getResultCode();
                    boolean success = loginInfo.isSuccess();
                    if (success) {
                        String stateMessage = ServerResponseState.getStateMessage(resultCode);

                        if ("0".equals(resultCode)) {
                            saveLoginInfo(loginInfo);
                            LoginInfo.DataBean data = loginInfo.getData();
                            String token = data.getToken();
                            NetApi.setToken(token);
                            Intent intent = new Intent(mContext, MainActivity.class);
                            activityJumps(intent);
                        } else {
                            ToastUtil.show(mContext, stateMessage);
                        }
                    } else {
                        ToastUtil.show(mContext, "登录失败");
                    }
                }

                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.showCenter(mContext, "登录失败");
                    LoadingDialog.newInstance().dismiss();
                }
            });
        }
        String path = NetApi.App.LOGIN;
        RetrofitService loginService = retrofitManage.createLoginService();
        Observable<Response<ResponseBody>> responseObservable = loginService.postFormNet(path, paraMap);
        responseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataNetObserver);

    }

    private void saveLoginInfo(LoginInfo loginInfo) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account_ed.getText().toString().trim());
        userInfo.setPassword(password_ed.getText().toString().trim());
        boolean remember = SpUtil.getBoolean(mContext, ConstantValues.UserInfo.key_remember_pwd, false);

        SpUtil.putString(mContext, ConstantValues.UserInfo.KEY_USER_PWD, userInfo.getPassword());
        SpUtil.putString(mContext, ConstantValues.UserInfo.KEY_USER_ACCOUNT, userInfo.getAccount());

        if(null != loginInfo) {
            SpUtil.putString(mContext, ConstantValues.UserInfo.KEY_USER_USERNAME, loginInfo.getData().getUsername());
            SpUtil.putString(mContext, ConstantValues.UserInfo.KEY_USER_ORG, loginInfo.getData().getOrgName());
        }
       // Log.e(TAG,"username:"+loginInfo.getData().getUsername()+"-account:"+userInfo.getAccount());
    }

    private void loadLoginInfo() {
        if (isRememberPwd) {
            String userName = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_ACCOUNT, "");
            String pwd = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_PWD, "");
            account_ed.setText(userName);
            password_ed.setText(pwd);
            account_ed.setSelection(userName.length());
        }
    }


    public void OnLongClick(View v){
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入路径")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        //Toast.makeText(getApplicationContext(), et.getText().toString(),Toast.LENGTH_LONG).show();
                        NetBaseUrl.setBaseURL(et.getText().toString());
                    }
                }).setNegativeButton("取消",null).show();
    }



}
