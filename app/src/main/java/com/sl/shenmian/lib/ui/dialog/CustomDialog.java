package com.sl.shenmian.lib.ui.dialog;
import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sl.shenmian.R;
import com.sl.shenmian.lib.ui.dialog.base.BaseDialogFragment;

/**
 * @author WenXian Bai
 * @Date: 2018/3/27.
 * @Description:自定义dialog
 */

public class CustomDialog extends BaseDialogFragment {

    private RelativeLayout dialog_tile_view;
    private TextView dialog_tile_tv;
    private ImageView dialog_title_btn;

    private TextView dialog_content_msg;
    private ImageView dialog_content_icon;

    private Button dialog_left_btn;
    private Button dialog_right_btn;

    private boolean mTitleViewIsShow = true;
    private boolean mContentIconIsShow = true;
    private boolean mRightBtnIsShow = true;
    private View.OnClickListener mDialogTitleBtnOnClick = null;
    private View.OnClickListener mDialogLefttBtnOnClick = null;
    private View.OnClickListener mDialogRightBtnOnClick = null;
    private int mDialogContentIcon = -1;
    private int mDialogContentMsg = -1;
    private int mDialogLeftBtnText = -1;
    private int mDialogRightBtnText = -1;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.Dialog_Bottom);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_custom_dialog,container,false);
        dialog_tile_view  = (RelativeLayout) view.findViewById(R.id.dialog_tile_view);
        dialog_tile_tv  = (TextView) view.findViewById(R.id.dialog_tile_tv);
        dialog_title_btn  = (ImageView) view.findViewById(R.id.dialog_title_btn);
        dialog_content_msg  = (TextView) view.findViewById(R.id.dialog_content_msg);
        dialog_content_icon  = (ImageView) view.findViewById(R.id.dialog_content_icon);
        dialog_left_btn  = (Button) view.findViewById(R.id.dialog_left_btn);
        dialog_right_btn  = (Button) view.findViewById(R.id.dialog_right_btn);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getDialog().setContentView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null != dialog_tile_view){
            dialog_tile_view.setVisibility(mTitleViewIsShow == true ? View.VISIBLE : View.GONE);
        }
        if(null != dialog_content_icon) {
            dialog_content_icon.setVisibility(mContentIconIsShow == true ? View.VISIBLE : View.GONE);
        }
        if(null != dialog_right_btn) {
            dialog_right_btn.setVisibility(mRightBtnIsShow == true ? View.VISIBLE : View.GONE);
        }
        if(null != dialog_title_btn && null != mDialogTitleBtnOnClick) {
            dialog_title_btn.setOnClickListener(mDialogTitleBtnOnClick);
        }
        if(null != dialog_left_btn && null != mDialogLefttBtnOnClick){
            dialog_left_btn.setOnClickListener(mDialogLefttBtnOnClick);
        }
        if(null != dialog_right_btn && null != mDialogRightBtnOnClick) {
            dialog_right_btn.setOnClickListener(mDialogRightBtnOnClick);
        }
        if(null != dialog_content_icon && mDialogContentIcon != -1){
            dialog_content_icon.setBackgroundResource(mDialogContentIcon);
        }
        if(null != dialog_content_msg && mDialogContentMsg != -1){
            dialog_content_msg.setText(mDialogContentMsg);
        }
        if(null != dialog_left_btn && mDialogLeftBtnText != -1){
            dialog_left_btn.setText(mDialogLeftBtnText);
        }
        if(null != dialog_right_btn && mDialogRightBtnText != -1){
            dialog_right_btn.setText(mDialogRightBtnText);
        }
    }

    /**
     * 是否显示头部
     * @param isShow
     */
    public void setTileViewShow(boolean isShow){
        //dialog_tile_view.setVisibility(isShow == true ? View.VISIBLE : View.GONE);
        mTitleViewIsShow  = isShow;
    }

    /**
     * 是否显示内容图标
     * @param isShow
     */
    public void setContentIconIsShow(boolean isShow){
       // dialog_content_icon.setVisibility(isShow == true ? View.VISIBLE : View.GONE);
        mContentIconIsShow = isShow;
    }

    /**
     * 是否显示right按钮
     * @param isShow
     */
    public void setRightBtnIsShow(boolean isShow){
        //dialog_right_btn.setVisibility(isShow == true ? View.VISIBLE : View.GONE);
        mRightBtnIsShow  = isShow;
    }

    /**
     * 设置头部按钮事件
     * @param onClickListener
     */
    public void setDialogTitleBtnOnClick(View.OnClickListener onClickListener){
       // dialog_title_btn.setOnClickListener(onClickListener);
        mDialogTitleBtnOnClick = onClickListener;
    }

    /**
     * 设置Left按钮事件
     * @param onClickListener
     */
    public void setDialogLeftBtnOnClick(View.OnClickListener onClickListener){
        mDialogLefttBtnOnClick = onClickListener;
    }

    /**
     * 设置Right按钮事件
     * @param onClickListener
     */
    public void setDialogRightBtnOnClick(View.OnClickListener onClickListener){
        //dialog_right_btn.setOnClickListener(onClickListener);
        mDialogRightBtnOnClick = onClickListener;
    }

    /**
     * 设置图标
     * @param resId
     */
    public void setDialogContentIcon(int resId){
        //dialog_content_icon.setBackgroundColor(resId);
        mDialogContentIcon = resId;
    }

    /**
     * 设置显示消息内容
     * @param resId
     */
    public void setDialogContentMsg(int resId){
        //dialog_content_msg.setText(resId);
        mDialogContentMsg = resId;
    }

    /**
     * 设置left按钮文字
     * @param resId
     */
    public void setDialogLeftBtnText(int resId){
        //dialog_left_btn.setText(resId);
        mDialogLeftBtnText = resId;
    }

    /**
     * 设置Right按钮文字
     * @param resId
     */
    public void setDialogRightBtnText(int resId){
        //dialog_right_btn.setText(resId);
        mDialogRightBtnText = resId;
    }

}
