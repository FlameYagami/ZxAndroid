package com.zx.uitls;

import android.content.Context;
import android.os.Environment;

import com.zx.R;

import java.io.File;

/**
 * Created by 八神火焰 on 2017/3/6.
 */

public class PathManager
{
    public static String appCache;
    public static String pictureCache;
    public static String pictureZipPath;
    public static String databasePath;
    public static String deckPath;
    public static String downloadPath;
    public static String banlistPath;

    public static void init(Context context) {
        appCache = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getString(R.string.app_name) + File.separator;
        pictureCache = appCache + context.getString(R.string.picture);
        pictureZipPath = pictureCache + context.getString(R.string.zip_extension);
        databasePath = "data/data/" + context.getPackageName() + "/databases/data.db";
        deckPath = appCache + "deck/";
        downloadPath = appCache + "download/";
        banlistPath = appCache + "banlist";
    }
}
