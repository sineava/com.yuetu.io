package org.spring.boot.multiple.ds.util;

import org.spring.boot.multiple.ds.bean.DateInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

    private static Calendar cal,cal1,cal2,cal3;
    private static SimpleDateFormat sdf,sdf1,sdf2,sdf3;

    static {
        cal = Calendar.getInstance();
        cal1 = Calendar.getInstance();
        cal2 = Calendar.getInstance();
        cal3 =  Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM");
        sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        sdf2 = new SimpleDateFormat("yyyy-MM");
        sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取日期相关数据
     */
    public static DateInfo dateData() {
        DateInfo info = new DateInfo();
        info.setCurrentYear(String.valueOf(cal.get(Calendar.YEAR)));
        info.setCurrentMonth(String.valueOf(cal.get(Calendar.MONTH)+1));
        info.setCurrentMonth25(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-25");

        cal1.add(Calendar.MONTH, -1);
        info.setLastMonth26(sdf.format(cal1.getTime())+"-26");

        int month=cal2.get(Calendar.MONTH);
        cal2.set(Calendar.MONTH, month-1);
        cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal2.set(Calendar.DAY_OF_MONTH,1);
        info.setLastMonthLastDay(sdf1.format(cal2.getTime()));
        info.setLastMonthFirstDay(sdf1.format(cal2.getTime()));

        info.setCurrentMonth1(sdf2.format(cal3.getTime())+"-01");
        info.setCurrentMonth26(sdf2.format(cal3.getTime())+"-26");

        info.setCurrentTime(sdf3.format(new Date()));
        return info;
    }
}
