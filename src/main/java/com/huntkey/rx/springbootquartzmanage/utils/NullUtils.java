package com.huntkey.rx.springbootquartzmanage.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * NullUtils 用于处理null的情况
 *
 * @author yaoss
 * @Date 2017/12/11 21:28
 * @@Description
 */
public class NullUtils {
    /**
     * null 变成 ""，否则toString
     *
     * @param o
     * @return
     */
    public static String valueOf(Object o) {
        if (null == o) {
            return "";
        }
        return o.toString();
    }

    /**
     * null变成"",如果是浮点型，变成int型字符串
     *
     * @param o
     * @return
     */
    public static String intValueOf(Object o) {
        if (null == o) {
            return "";
        }
        if (o instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) o;
            return "" + bigDecimal.intValue();
        }
        return o.toString();
    }

    public static DecimalFormat format = new DecimalFormat(",###.#######");
    public static SimpleDateFormat sdf = new SimpleDateFormat();

    /**
     * null变成"",浮点型数据的处理
     *
     * @param o
     * @return
     */
    public static String doubleValueOf(Object o) {
        if (null == o) {
            return "";
        }
        if (o instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) o;
            return format.format(Double.parseDouble(bigDecimal.toPlainString()));
        }
        return o.toString();
    }

    /**
     * 格式化时间，显示没有具体的时分秒
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static Date formatDateNoTime(Date date) throws Exception {
        sdf.applyPattern("yyyy-MM-dd");
        String str = sdf.format(date);
        return sdf.parse(str);
    }

    /**
     * 不设置时间，有时需要理解成无穷大，比如失效日期
     *
     * @return
     * @throws Exception
     */
    public static Date nullEndDate() throws Exception {
        sdf.applyPattern("yyyy-MM-dd");
        return sdf.parse("9999-12-31");
    }

    /**
     * 时间段 使开始时间 的时分秒为 00:00:00
     *
     * @param startTime
     * @return
     * @throws Exception
     */
    public static String convertStartTime(String startTime) throws Exception {
        sdf.applyPattern("yyyy-MM-dd");
        Date date1 = sdf.parse(startTime);
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date1);
    }

    /**
     * 时间段 使结束时间 的时分秒为 23:59:59
     *
     * @param endTime
     * @return
     * @throws Exception
     */
    public static String convertEndTime(String endTime) throws Exception {
        sdf.applyPattern("yyyy-MM-dd");
        Date date1 = sdf.parse(endTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }


}
