package com.sl.shenmian.lib.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sl.shenmian.R;
import com.sl.shenmian.lib.ui.dialog.base.BaseDialogFragment;

public class ImageDialog extends BaseDialogFragment {

    private ImageView dialog_title_btn;
    private ImageView show_image_imag;
    private View.OnClickListener mDialogTitleBtnOnClick = null;

    private String imageUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View v = inflater.inflate(R.layout.layout_image_view, container,false);
        dialog_title_btn = v.findViewById(R.id.dialog_title_btn);
        show_image_imag = v.findViewById(R.id.show_image_imag);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null != dialog_title_btn && null != mDialogTitleBtnOnClick) {
            dialog_title_btn.setOnClickListener(mDialogTitleBtnOnClick);
        }

        if(null != show_image_imag && null != imageUrl) {
            Glide.with(getActivity())
                    .load(imageUrl)
                    .asBitmap()//只加载静态图片，如果是git图片则只加载第一帧。
                    .placeholder(R.mipmap.loading)
                    .error(R.mipmap.fail)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(show_image_imag);
        }
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
     * 设置要显示的图片路径
     * @param imageUrl
     */
    public void setImagUrl(String imageUrl){
        this.imageUrl  = imageUrl;
    }
}
