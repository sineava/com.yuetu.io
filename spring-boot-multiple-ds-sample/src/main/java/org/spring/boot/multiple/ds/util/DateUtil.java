package org.spring.boot.multiple.ds.util;

import org.spring.boot.multiple.ds.bean.DateInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        Calendar cal2 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        cal1.add(Calendar.MONTH, -1);
        DateInfo info = new DateInfo();
        info.setCurrentYear(String.valueOf(cal.get(Calendar.YEAR)));
        info.setCurrentMonth(String.valueOf(cal.get(Calendar.MONTH)+1));
        info.setCurrentMonth25(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-25");
        info.setLastMonth26(sdf.format(cal1.getTime())+"-26");

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        int month=cal2.get(Calendar.MONTH);
        cal2.set(Calendar.MONTH, month-1);
        cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
        info.setLastMonthLastDay(sdf1.format(cal2.getTime()));
        cal2.set(Calendar.DAY_OF_MONTH,1);
        info.setLastMonthFirstDay(sdf1.format(cal2.getTime()));

        Calendar cal3 = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        info.setCurrentMonth1(sdf2.format(cal3.getTime())+"-01");
        info.setCurrentMonth26(sdf2.format(cal3.getTime())+"-26");

        Date date = new Date();
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        info.setCurrentTime(sdf3.format(date));
        return info;
    }
}
