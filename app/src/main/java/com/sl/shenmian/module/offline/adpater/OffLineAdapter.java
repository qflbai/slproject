package com.sl.shenmian.module.offline.adpater;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sl.shenmian.R;
import com.sl.shenmian.module.offline.model.OfflineInfo;

import java.util.List;


/**
 * @author WenXian Bai
 * @Date: 2018/5/14.
 * @Description: 扫码收货详情
 */
public class OffLineAdapter extends BaseAdapter {
    private List<OfflineInfo> mData;
    private Context mContext;

    public OffLineAdapter(Context context, List<OfflineInfo> data) {
        mData = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        if (convertView == null) {
            holderView = new HolderView();
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.listview_item_offlien, null);


            TextView tv_coding = inflate.findViewById(R.id.tv_coding);
            TextView tv_address = inflate.findViewById(R.id.tv_address);
            TextView tv_time = inflate.findViewById(R.id.tv_time);
            TextView tv_uploading_stae = inflate.findViewById(R.id.tv_uploading_stae);
            TextView tv_remark = inflate.findViewById(R.id.tv_remark);
            holderView.tv_coding = tv_coding;
            holderView.tv_address = tv_address;
            holderView.tv_time = tv_time;
            holderView.tv_uploading_stae = tv_uploading_stae;
            holderView.tv_remark = tv_remark;
            convertView = inflate;
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        OfflineInfo offlineInfo = mData.get(position);
        holderView.tv_coding.setText(offlineInfo.getCoding());
        holderView.tv_address.setText(offlineInfo.getAddress());
        holderView.tv_time.setText(offlineInfo.getTime());
        if(0==offlineInfo.getUploadingStae()){
            holderView.tv_uploading_stae.setText("未上传");
            holderView.tv_uploading_stae.setTextColor(Color.WHITE);
            holderView.tv_uploading_stae.setBackgroundColor(mContext.getResources().getColor(R.color._fff82624));
        }else {
            holderView.tv_uploading_stae.setText("已上传");
        }

        holderView.tv_remark.setText(offlineInfo.getRemark());

        return convertView;
    }


    private static class HolderView {
        public TextView tv_coding;
        public TextView tv_address;
        public TextView tv_time;
        public TextView tv_uploading_stae;
        public TextView tv_remark;
    }
}
