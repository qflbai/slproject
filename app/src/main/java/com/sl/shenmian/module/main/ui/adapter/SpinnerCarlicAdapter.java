package com.sl.shenmian.module.main.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sl.shenmian.R;
import com.sl.shenmian.module.main.pojo.CarLic;
import com.sl.shenmian.module.main.pojo.Station;

import java.util.ArrayList;
import java.util.List;


public class SpinnerCarlicAdapter extends BaseAdapter {

    private List<CarLic> mlist = new ArrayList<>();
    private Context mContext;

    public SpinnerCarlicAdapter(Context context, List<CarLic> list){
        this.mlist = list;
        this.mContext = context;
    }

    public void setData(List<CarLic> list){
        this.mlist.clear();
        this.mlist.addAll(list);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if(null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_list_item,parent,false);
            viewHodler = new ViewHodler();
            viewHodler.textView = convertView.findViewById(R.id.textView);
            convertView.setTag(viewHodler);
        }else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        CarLic carLic = mlist.get(position);
        viewHodler.textView.setText(carLic.getCarLic());
        return convertView;
    }

    static  class ViewHodler{
        public TextView textView;
    }
}
