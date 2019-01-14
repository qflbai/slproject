package com.sl.shenmian.lib.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sl.shenmian.R;
import com.sl.shenmian.lib.ui.dialog.base.BaseDialogFragment;

public class MenuDialog extends BaseDialogFragment {


    private LinearLayout signature_menu,photo_menu,cancel_menu;
    private View.OnClickListener onClickListener1 = null;
    private View.OnClickListener onClickListener2 = null;
    private ImageView dialog_title_btn;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.Dialog_Bottom);
        return dialog;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View v = inflater.inflate(R.layout.choose_menu_dialog_view, container,false);
        signature_menu = v.findViewById(R.id.signature_menu);
        photo_menu = v.findViewById(R.id.photo_menu);
        cancel_menu = v.findViewById(R.id.cancel_menu);
        dialog_title_btn = v.findViewById(R.id.dialog_title_btn);
        cancel_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        dialog_title_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null != signature_menu && null != onClickListener1) {
            signature_menu.setOnClickListener(onClickListener1);
        }
        if(null != photo_menu && null != onClickListener2) {
            photo_menu.setOnClickListener(onClickListener2);
        }
    }

    /**
     * 设置头部按钮事件
     * @param onClickListener
     */
    public void setSignatureMenuOnClickListener(View.OnClickListener onClickListener){
        onClickListener1 = onClickListener;
    }
    /**
     * 设置头部按钮事件
     * @param onClickListener
     */
    public void setPhotoMenuOnClickListener(View.OnClickListener onClickListener){
        onClickListener2 = onClickListener;
    }

}
