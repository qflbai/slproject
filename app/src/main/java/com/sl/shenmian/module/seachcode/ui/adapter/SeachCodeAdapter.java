package com.sl.shenmian.module.seachcode.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sl.shenmian.R;
import com.sl.shenmian.module.seachcode.pojo.SeachCodeInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeachCodeAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<SeachCodeInfo> mlist = new ArrayList<>();
    public SeachCodeAdapter(Context context){
        this.mContext = context;
    }

    public void setData(List<SeachCodeInfo> list){
        mlist.clear();
        mlist.addAll(list);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(mContext).inflate(R.layout.seach_code_result_list_item,parent,false);
         viewHodler viewHodler = new viewHodler(view);
         return viewHodler;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vhodler, int position) {

        viewHodler viewHodler = (viewHodler)vhodler;
        SeachCodeInfo seachCodeInfo = mlist.get(position);
        if(null != seachCodeInfo && null != seachCodeInfo.getUsername() && seachCodeInfo.getUsername().length() > 0){
            viewHodler.clearance_user_tv.setText(seachCodeInfo.getUsername());
        }else {
            viewHodler.clearance_user_tv.setText("");
        }
        if(null != seachCodeInfo && null != seachCodeInfo.getAddr() && seachCodeInfo.getAddr().length() > 0){
            viewHodler.clearance_addr_tv.setText(seachCodeInfo.getAddr());
        }else {
            viewHodler.clearance_addr_tv.setText("");
        }
        if(null != seachCodeInfo && null != seachCodeInfo.getTime() && seachCodeInfo.getTime().length() > 0){
            viewHodler.clearance_time_tv.setText(seachCodeInfo.getTime());
        }else {
            viewHodler.clearance_time_tv.setText("");
        }
        if(null != seachCodeInfo && null != seachCodeInfo.getLic() && seachCodeInfo.getLic().length() > 0){
            viewHodler.car_number_tv.setText(seachCodeInfo.getTime());
        }else {
            viewHodler.car_number_tv.setText("");
        }
        if(null != seachCodeInfo && null != seachCodeInfo.getRemark() && seachCodeInfo.getRemark().length() > 0){
            viewHodler.remark_tv.setText(seachCodeInfo.getTime());
        }else {
            viewHodler.remark_tv.setText("");
        }

        if(position  > 0){
            viewHodler.clearance_user.setText(mContext.getString(R.string.out_user));
            viewHodler.clearance_addr.setText(mContext.getString(R.string.out_addr));
            viewHodler.clearance_time.setText(mContext.getString(R.string.out_time));
        }else {
            viewHodler.clearance_user.setText(mContext.getString(R.string.clearance_user));
            viewHodler.clearance_addr.setText(mContext.getString(R.string.clearance_addr1));
            viewHodler.clearance_time.setText(mContext.getString(R.string.clearance_time));
        }
        viewHodler.show_signature_list.setTag(viewHodler);
        viewHodler.show_signature_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewHodler viewHodler1 = (viewHodler)v.getTag();
                if(viewHodler1.signature_list_view.getVisibility() == View.VISIBLE){
                    viewHodler1.signature_list_view.setVisibility(View.GONE);
                }else {
                    viewHodler1.signature_list_view.setVisibility(View.VISIBLE);
                }
            }
        });
        if(seachCodeInfo.getImg().size() > 0){
            for(SeachCodeInfo.ImgBean image : seachCodeInfo.getImg()){
                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(35,35));
                imageView.setPadding(2,2,2,2);
                //imageView.setim
                Glide.with(mContext)
                        .load(image.getThumbUrl())
                        .asBitmap()//只加载静态图片，如果是git图片则只加载第一帧。
                        .placeholder(R.mipmap.loading)
                        .error(R.mipmap.fail)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    static class viewHodler extends RecyclerView.ViewHolder{
        @BindView(R.id.clearance_user_tv) TextView clearance_user_tv;
        @BindView(R.id.clearance_addr_tv) TextView clearance_addr_tv;
        @BindView(R.id.clearance_time_tv)TextView clearance_time_tv;
        @BindView(R.id.clearance_user) TextView clearance_user;
        @BindView(R.id.clearance_addr) TextView clearance_addr;
        @BindView(R.id.clearance_time)TextView clearance_time;
        @BindView(R.id.car_number_tv) TextView car_number_tv;
        @BindView(R.id.remark_tv) TextView remark_tv;
        @BindView(R.id.show_signature_list) TextView show_signature_list;
        @BindView(R.id.signature_list_view) GridLayout signature_list_view;

        public viewHodler(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
