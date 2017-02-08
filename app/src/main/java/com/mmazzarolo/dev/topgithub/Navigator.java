package com.mmazzarolo.dev.topgithub;

import android.content.Context;
import android.content.Intent;

import com.mmazzarolo.dev.topgithub.activity.SimpleWebActivity;

/**
 * Created by Arison on 2017/2/8.
 * app 常量以及界面调转公共方法
 */
public class Navigator {

    public final static String EXTRA_WEB_URL = "extra_web_url";
    public final static String EXTRA_HTML_STRING = "extra_html_string";


    public static void startWebActivity(Context context, String url) {
        Intent intent = new Intent(context, SimpleWebActivity.class);
        intent.putExtra(EXTRA_WEB_URL, url);
        context.startActivity(intent);
    }
}
