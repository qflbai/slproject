package com.sl.shenmian.module.signature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.kyanogen.signatureview.SignatureView;
import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.lib.utils.image.BitmapUtils;
import com.sl.shenmian.lib.utils.toast.ToastUtil;
import com.sl.shenmian.module.commons.Constants;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

import static com.sl.shenmian.lib.constant.ActivityConstant.ACTIVITY_SIGNAGURE_KEY;

/**
 * 签名
 */
public class SignatureActivity extends BaseActivity {

    @BindView(R.id.signature_view)
    SignatureView mSignatureView;
    @BindView(R.id.signature_cancel_btn)
    Button signature_cancel_btn;
    @BindView(R.id.signature_clean_btn)
    Button signature_clean_btn;
    @BindView(R.id.signature_completed_btn)
    Button signature_completed_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signature_view);
    }

    @OnClick({R.id.signature_cancel_btn,R.id.signature_clean_btn,R.id.signature_completed_btn})
    void onClick(View v){
        switch (v.getId()){
            case R.id.signature_cancel_btn:
                    finish();
                break;
            case R.id.signature_clean_btn:
                    signatureClean();
                break;
            case R.id.signature_completed_btn:

                    saveSignatureBitmap();
                break;
        }

    }

    /**
     * 清除
     */
    private void signatureClean(){
        if(null != mSignatureView) {
            mSignatureView.clearCanvas();
        }
    }

    /**
     * 保存签名文件
     */
    private void saveSignatureBitmap(){
        if(!mSignatureView.isBitmapEmpty()) {
            Bitmap bitmap = mSignatureView.getSignatureBitmap();
            String filePath = Constants.LocalFile.IMAGE_PATH;
            String fileName = filePath+System.currentTimeMillis()+".jpg";
            bitmap = BitmapUtils.compressScale(bitmap);

            File directory = new File(filePath);
            if(!directory.exists()){
                directory.mkdirs();
            }
            File file = new File(fileName);
            try {
                BitmapUtils.saveBitmapToJPG(bitmap,file);
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtil.show(SignatureActivity.this,getString(R.string.signature_save_error));
            }

            Intent intent = new Intent();
            intent.putExtra(ACTIVITY_SIGNAGURE_KEY,fileName);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
