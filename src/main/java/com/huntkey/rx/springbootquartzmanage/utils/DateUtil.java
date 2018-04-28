package com.huntkey.rx.springbootquartzmanage.utils;

import org.springframework.expression.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunwei on 2018-02-27 Time:8:50:28
 */
public class DateUtil {
    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
