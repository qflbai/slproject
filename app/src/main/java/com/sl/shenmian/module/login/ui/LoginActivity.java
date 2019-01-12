package com.sl.shenmian.module.login.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.AbsActivity;
import com.sl.shenmian.lib.constant.ConstantValues;
import com.sl.shenmian.lib.utils.sharedpreferences.SpUtil;
import com.sl.shenmian.lib.utils.toast.ToastUtil;
import com.sl.shenmian.module.login.pojo.UserInfo;
import com.sl.shenmian.module.login.viewmodel.LoginViewModel;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends AbsActivity<LoginViewModel> {

    @BindView(R.id.editText2)
    EditText account_ed;
    @BindView(R.id.editText3)
    EditText password_ed;
    @BindView(R.id.checkBox)
    CheckBox re_password_ck;
    @BindView(R.id.button)
    Button button;

    //记住密码
    private boolean isRePASSWORD = true;

    //相机权限 手机权限 定位权限gps 网络 存储权限
    private String[] mPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private boolean mIsHaveAllPermissions = false;

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

    private void initUI(){
        isRePASSWORD = re_password_ck.isChecked();
        re_password_ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRePASSWORD = isChecked;
            }
        });
    }

    private void initCheckPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissionAllGranted(mPermissions)) {
                mIsHaveAllPermissions = false;
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
    void onClick(View v){

        switch (v.getId()){
            case R.id.button:
                    if(checkUserInfo()){
                        login();
                    }
                break;
        }
    }

    private boolean checkUserInfo(){
        String account = account_ed.getText().toString().trim();
        String password = password_ed.getText().toString().trim();
        String acc_acc_parent = "/^[A-Za-z0-9]{3,10}$/";
        String acc_pwd_parent = "/^[A-Za-z0-9]{3,14}$/";

        boolean checked = false;
        if(account.length() == 0){
            if(acc_acc_parent.matches(acc_acc_parent)){
                checked = true;
            }else {
                ToastUtil.showCenter(LoginActivity.this,getString(R.string.login_user_account_error_regex));
            }
        }else {
            ToastUtil.showCenter(LoginActivity.this,getString(R.string.login_user_account_error_empty));
        }
        if(password.length() == 0){
            if(acc_acc_parent.matches(acc_pwd_parent)){
                checked = true;
            }else {
                ToastUtil.showCenter(LoginActivity.this,getString(R.string.login_user_password_error_regex));
            }
        }else {
            ToastUtil.showCenter(LoginActivity.this,getString(R.string.login_user_password_error_empty));
        }
        return checked;
    }

    private void login(){

    }

    private void saveLoginInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account_ed.getText().toString().trim());
        userInfo.setPassword(account_ed.getText().toString().trim());

        SpUtil.putString(LoginActivity.this, ConstantValues.UserInfo.KEY_USER_ACCOUNT, JSON.toJSONString(userInfo));
    }

    private void loadLoginInfo(){
        String info = SpUtil.getString(LoginActivity.this,ConstantValues.UserInfo.KEY_USER_ACCOUNT,"");
        if(null != info && info.length() > 0){
            UserInfo userInfo = JSON.parseObject(info,UserInfo.class);
            if(null != userInfo && null != userInfo.getAccount() && userInfo.getAccount().length() > 0
                   && null != userInfo.getPassword() && userInfo.getPassword().length() > 0) {
                account_ed.setText(userInfo.getAccount());
                password_ed.setText(userInfo.getPassword());
            }
        }
    }
}
