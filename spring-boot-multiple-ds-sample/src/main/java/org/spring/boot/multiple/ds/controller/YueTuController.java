package org.spring.boot.multiple.ds.controller;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;
import org.spring.boot.multiple.ds.service.MerchandiseShiftService;
import org.spring.boot.multiple.ds.service.YueTuService;
import org.spring.boot.multiple.ds.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * EnableScheduling:开启定时任务
 * @author 刘世杰
 */
@EnableScheduling
@RestController
@Component
public class YueTuController {

    @Autowired
    private YueTuService yueTuService;

    @Autowired
    private MerchandiseShiftService merchandiseShiftService;

    /**
     * 抽取商品进货单据
     * 每月25日定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 25 * ?")
    @RequestMapping("/goodsReceiptVoucher")
    public String goodsReceiptVoucher() {
        String msg = "success";
        try {
            //日期相关数据
            DateInfo date = DateUtil.dateData();
            List<Tvoucher> tVoucherList = yueTuService.goodsReceiptVoucher(date);
            List<TvoucherEntry> tVoucherEntryList = yueTuService.goodsReceiptVoucherEntry(date);
            yueTuService.insertGoodsReceiptVoucher(tVoucherList,tVoucherEntryList);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }

    /**
     * 抽取商品退货单据
     * 每月25日定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 25 * ?")
    @RequestMapping("/goodsReturnReceiptVoucher")
    public String goodsReturnReceipt() {
        String msg = "success";
        try {
            //日期相关数据
            DateInfo date = DateUtil.dateData();
            List<Tvoucher> tVoucherList = yueTuService.goodsReturnReceiptVoucher(date);
            List<TvoucherEntry> tVoucherEntryList = yueTuService.goodsReturnVoucherEntry(date);
            yueTuService.insertGoodsReturnReceiptVoucher(tVoucherList,tVoucherEntryList);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }

    /**
     * 抽取商品移仓单单据
     * 每月25日定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 25 * ?")
    @RequestMapping("/merchandiseShiftVoucher")
    public String merchandiseShiftVoucher() {
        String msg = "success";
        try {
            //日期相关数据
            DateInfo date = DateUtil.dateData();
            List<Tvoucher> tVoucherList = merchandiseShiftService.merchandiseShiftVoucher(date);
            List<TvoucherEntry> tVoucherEntryList = merchandiseShiftService.merchandiseShiftVoucherEntry(date);
            merchandiseShiftService.insertMerchandiseShiftVoucher(tVoucherList,tVoucherEntryList);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }
}
