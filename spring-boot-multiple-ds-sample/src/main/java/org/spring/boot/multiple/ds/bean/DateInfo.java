package org.spring.boot.multiple.ds.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 日期相关信息
 * @author 刘世杰
 * @date 2019/8/13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateInfo {

    /**本月25日(yyyy-mm-dd hh:mm:sss)*/
    private String currentMonth25;
    /**上个月26日(yyyy-mm-dd hh:mm:sss)*/
    private String lastMonth26;

    /**
     * add: 2019/8/19
     * 用于获取当前年月
     */
    private String currentYear;
    private String currentMonth;

    /**
     * add: 2019/8/28
     * 用于获取上月1号与最后一天
     */
    private String lastMonthFirstDay;
    private String lastMonthLastDay;
}
