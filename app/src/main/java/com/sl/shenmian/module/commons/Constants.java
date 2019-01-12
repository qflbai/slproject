package com.sl.shenmian.module.commons;

import android.os.Environment;

import com.sl.shenmian.module.main.pojo.CarLic;
import com.sl.shenmian.module.main.pojo.Station;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static class LocalFile{
        //SD卡路径
        public final static String SDPATH = Environment.getExternalStorageDirectory().getPath()
                + File.separator+"SL"+ File.separator;
        public final static String IMAGE_PATH = SDPATH + "images"+File.separator;
    }

    /**
     * 站点信息
     */
    public static class LocalStation{
        /**
         * 关口地址
         */
        public static List<Station>  customs = new ArrayList<>();
        /**
         * 仓库地址
         */
        public static List<Station>  wareHouses = new ArrayList<>();
        /**
         * 门店地址
         */
        public static List<Station>  stores = new ArrayList<>();
    }

    /**
     * 车牌信息
     */
    public static class LocalCarLic{
        /**
         * 关口地址
         */
        public static List<CarLic>  carLics = new ArrayList<>();
    }
}
