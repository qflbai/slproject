package com.sl.shenmian.lib.base.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.LibBaseActivity;
import com.sl.shenmian.lib.utils.BarUtils;

import butterknife.ButterKnife;


/**
 * @author WenXian Bai
 * @Date: 2018/11/2.
 * @Description:
 */

public class BaseActivity extends LibBaseActivity {

    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initViews(mSavedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化views
     *
     * @param savedInstanceState
     */
    public void initViews(Bundle savedInstanceState) {

    }


    /**
     * 状态栏初始化
     */
    protected void initStatusBar() {
        BarUtils.setStatusBarAlpha(this);
    }


    /**
     * 初始化toolbar(在子类中调用)
     *
     * @param title 标题
     */
    protected void initBackToolbar(CharSequence title) {
        Toolbar toolbar = getToolbar();
        setToolbarTitle(title);
        /*toolbar.setNavigationIcon(R.mipmap.ic_title_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        });*/
    }


    /**
     * 获取toolbar
     *
     * @return
     */
    protected Toolbar getToolbar() {
        return findViewById(R.id.toolbar_title);
    }

    /**
     * 得到标题 textview
     *
     * @return
     */
    protected TextView getTitleTextView() {
        return findViewById(R.id.tv_title);
    }

    /**
     * 获取标题imagebutton
     *
     * @return
     */
    protected ImageButton getTitleImageButton() {
        return findViewById(R.id.title_ib_ic);
    }

    /**
     * 获取标题button
     *
     * @return
     */
    protected Button getTitleButton() {
        return findViewById(R.id.title_btn_other);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setToolbarTitle(CharSequence title) {
        getTitleTextView().setText(title);
    }


    /**
     * 页面跳转
     */
    protected void activityJumps(Intent intent) {
        // overridePendingTransition(R.anim.zoom_in_avtivity_switchover, R.anim.zoom_out_avtivity_switchover);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    @Override
    protected void quitLogin() {

    }

    /**
     * 页面销毁
     */
    protected void onFinish() {
        finish();
    }

    protected void onExit(){
        onFinish();
        System.exit(0);
    }

    /**
     * 显示进度条
     */
    public void showProgressBar() {
    }

    /**
     * 隐藏进度条
     */
    public void hideProgressBar() {

    }

}
