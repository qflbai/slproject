package com.sl.shenmian.module.commons;

import android.os.Environment;

import java.io.File;

public class Constants {

    public static class LocalFile{
        //SD卡路径
        public final static String SDPATH = Environment.getExternalStorageDirectory().getPath()
                + File.separator+"SL"+ File.separator;
        public final static String IMAGE_PATH = SDPATH + "images"+File.separator;
    }
}
