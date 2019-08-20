package org.spring.boot.multiple.ds.util;

import org.spring.boot.multiple.ds.bean.DateInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 日期工具类
 */
public class DateUtil {
    /**
     * 获取日期相关数据
     */
    public static DateInfo dateData() {
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        cal1.add(Calendar.MONTH, -1);
        DateInfo info = new DateInfo();
        info.setCurrentYear(String.valueOf(cal.get(Calendar.YEAR)));
        info.setCurrentMonth(String.valueOf(cal.get(Calendar.MONTH)+1));
        info.setCurrentMonth25(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-25 00:00:000");
        info.setLastMonth26(sdf.format(cal1.getTime())+"-26 00:00:000");
        return info;
    }
}
