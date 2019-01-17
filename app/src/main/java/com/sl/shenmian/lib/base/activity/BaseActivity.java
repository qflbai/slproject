package com.sl.shenmian.lib.base.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.LibBaseActivity;
import com.sl.shenmian.lib.net.rxjava.BaseObserver;

import butterknife.ButterKnife;


/**
 * @author WenXian Bai
 * @Date: 2018/11/2.
 * @Description:
 */

public class BaseActivity extends LibBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       // BarUtils.setStatusBarAlpha(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
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
     * 初始化toolbar(在子类中调用)
     */
    protected void initToolbar() {
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.mipmap.back);
        //toolbar.setNavigationIcon(R.mipmap.back);
        getTitleImageButton().setBackground(getResources().getDrawable(R.mipmap.grzx));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化toolbar(在子类中调用)
     *
     * @param title 标题
     */
    protected void initToolbar(CharSequence title) {
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.mipmap.ic_title_back);
        toolbar.setBackground(getResources().getDrawable(R.color.colorPrimary));
        getTitleImageButton().setBackground(getResources().getDrawable(R.mipmap.grzx));
        setToolbarTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        });
    }

    protected void initBackToolbar(CharSequence title){
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.mipmap.ic_title_back);
        toolbar.setBackground(getResources().getDrawable(R.color.colorPrimary));
        getTitleImageButton().setBackground(getResources().getDrawable(R.mipmap.grzx));
        setToolbarTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        });
    }

    /**
     * 初始化toolbar(在子类中调用)
     *
     * @param title 标题
     */
    protected void initToolbarNoback(CharSequence title) {
        Toolbar toolbar = getToolbar();
        toolbar.setBackground(getResources().getDrawable(R.mipmap.ic_title_back));
        getTitleImageButton().setBackground(getResources().getDrawable(R.mipmap.grzx));
        setToolbarTitle(title);
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
    protected void activityJumps() {
        overridePendingTransition(R.anim.zoom_in_avtivity_switchover, R.anim.zoom_out_avtivity_switchover);
    }

    /**
     * 页面跳转
     */
    protected void activityJumps(Intent intent) {
        overridePendingTransition(R.anim.zoom_in_avtivity_switchover, R.anim.zoom_out_avtivity_switchover);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    @Override
    protected void quitLogin() {
       /* Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);*/
    }

    /**
     * 页面销毁
     */
    protected void onFinish() {
        finish();
    }


    /**
     * 关闭网络请求
     */
    protected void closeNet(BaseObserver baseObserver) {
        if (baseObserver != null) {
            baseObserver.closeNet();
        }
    }


    protected void showDialog(String title, String msg, String leftBtn, String rigthBtn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        View view = inflateView(R.layout.layout_custom_dialog);
        onDialogView(view);
        RelativeLayout dialog_tile_view  = (RelativeLayout) view.findViewById(R.id.dialog_tile_view);
        TextView dialog_tile_tv  = (TextView) view.findViewById(R.id.dialog_tile_tv);
        ImageView dialog_title_btn  = (ImageView) view.findViewById(R.id.dialog_title_btn);
        TextView dialog_content_msg  = (TextView) view.findViewById(R.id.dialog_content_msg);
        ImageView dialog_content_icon  = (ImageView) view.findViewById(R.id.dialog_content_icon);
        Button dialog_left_btn  = (Button) view.findViewById(R.id.dialog_left_btn);
        Button dialog_right_btn  = (Button) view.findViewById(R.id.dialog_right_btn);
        builder.setView(view);

        dialog_content_msg.setText(msg);
        dialog_tile_tv.setText(title);
        if(!leftBtn.isEmpty()) {
            dialog_left_btn.setText(leftBtn);
        }
        if(!rigthBtn.isEmpty()) {
            dialog_right_btn.setText(rigthBtn);
        }


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        dialog_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLeftClick(alertDialog);
            }
        });

        dialog_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRightClick(alertDialog);
            }
        });

        dialog_title_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTitleRight(alertDialog);
            }
        });
    }

    protected void onDialogView(View view){

    }

    protected void dialogTitleRight(AlertDialog alertDialog){

    }

    protected void dialogLeftClick(AlertDialog alertDialog){

    }

    protected void dialogRightClick(AlertDialog alertDialog){

    }

}
