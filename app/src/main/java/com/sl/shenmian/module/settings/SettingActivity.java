package com.sl.shenmian.module.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.lib.constant.ConstantValues;
import com.sl.shenmian.lib.net.RetrofitManage;
import com.sl.shenmian.lib.net.body.ServerResponseResult;
import com.sl.shenmian.lib.net.callback.DataNetCallback;
import com.sl.shenmian.lib.net.retrofit.RetrofitService;
import com.sl.shenmian.lib.net.rxjava.DataNetObserver;
import com.sl.shenmian.lib.net.url.NetApi;
import com.sl.shenmian.lib.utils.sharedpreferences.SpUtil;
import com.sl.shenmian.lib.utils.toast.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.et_3)
    TextView tvOrg;
    @BindView(R.id.et_2)
    TextView tvAccount;
    @BindView(R.id.et_1)
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUi();
        initData();
    }

    private void initData() {
        String account = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_ACCOUNT, "");

        String userName = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_USERNAME, "");
        String org = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_ORG, "");

        tvOrg.setText(org);
        tvName.setText(userName);
        tvAccount.setText(account);
    }

    private void initUi() {
        initBackToolbar("设置");

    }

    @OnClick({R.id.cardview_2})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardview_2:
                pwdWindow();
                break;
            default:
                break;
        }
    }

    private void pwdWindow(){
        View view = inflateView(R.layout.alertlog_amend_pwd);
        EditText et1 = view.findViewById(R.id.et_1);
        EditText et2 = view.findViewById(R.id.et_1);
        EditText et3 = view.findViewById(R.id.et_1);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("密码修改");
        builder.setPositiveButton("确认",null)
                .setNegativeButton("取消",null)
                .setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button cancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(verifyPwd(et1,et2,et3)){
                            alertDialog.dismiss();


                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }

    private boolean verifyPwd(EditText et1,EditText et2,EditText et3){
        String pwd1 = et1.getText().toString();
        String pwd2 = et2.getText().toString();
        String pwd3 = et3.getText().toString();
        if(pwd1.isEmpty()||pwd2.isEmpty()||pwd3.isEmpty()){
            ToastUtil.show(mContext,"密码不能为空");
            return false;
        }

        String pwd = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_PWD, "");
        if(!pwd1.equals(pwd)){
            ToastUtil.show(mContext,"原密码不对!");
            return false;
        }

        if(!pwd2.equals(pwd3)){
            ToastUtil.show(mContext,"两次密码输入不一致!");
            return false;
        }
        return true;
    }

    RetrofitManage retrofitManage  = new RetrofitManage();
    /**
     * 修改密码
     * @param pwd1
     * @param pwd2
     * @param pwd3
     */
    private void amendPwd(String pwd1,String pwd2,String pwd3){
        HashMap<String,Object> param = new HashMap<>();
        param.put("","");

        String path = "";

        RetrofitService service = retrofitManage.createService();
        Observable<Response<ResponseBody>> responseObservable = service.postFormNet(path, param);
        responseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DataNetObserver(mContext, new DataNetCallback() {
                    @Override
                    public void onOkResponse(String dataJson) {

                    }

                    @Override
                    public void onFailResponse(String dataJson, ServerResponseResult serverResponseResult) {

                    }

                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));

    }
}
