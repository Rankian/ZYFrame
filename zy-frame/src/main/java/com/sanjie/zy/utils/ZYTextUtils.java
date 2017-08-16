package com.sanjie.zy.utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LangSanJie on 2017/5/4.
 */

public class ZYTextUtils {

    public static SpannableStringBuilder colorText(String wholeText, String colorText, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(wholeText);

        //匹配规则
        Pattern p = Pattern.compile(colorText);
        //匹配字段
        Matcher m = p.matcher(builder);
        //上色
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            builder.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }

    public static SpannableStringBuilder colorText(String wholeText, int start, int end, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(wholeText);
        builder.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static SpannableStringBuilder colorText(String wholeText, int color) {
        return colorText(wholeText, wholeText, color);
    }
}
