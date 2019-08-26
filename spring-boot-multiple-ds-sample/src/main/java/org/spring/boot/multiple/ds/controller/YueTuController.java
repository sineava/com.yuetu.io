package org.spring.boot.multiple.ds.controller;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;
import org.spring.boot.multiple.ds.service.GoodsIntoReturnService;
import org.spring.boot.multiple.ds.service.IsExtractService;
import org.spring.boot.multiple.ds.service.MerchandiseShiftService;
import org.spring.boot.multiple.ds.service.ProductDistributionOrderService;
import org.spring.boot.multiple.ds.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * EnableScheduling:开启定时任务
 * @author 刘世杰
 * GoodsIntoReturnService:商品进退货
 * MerchandiseShiftService:商品移仓单
 * productDistributionOrder:商店配货退货单
 */
@EnableScheduling
@RestController
@Component
public class YueTuController {

    @Autowired
    private GoodsIntoReturnService goodsIntoReturnService;

    @Autowired
    private MerchandiseShiftService merchandiseShiftService;

    @Autowired
    private ProductDistributionOrderService productDistributionOrderService;

    @Autowired
    private IsExtractService isExtractService;

    /**
     * 抽取商品进货单据
     * 每月25日定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 25 * ?")
    @RequestMapping("/goodsReceiptVoucher")
    public String goodsReceiptVoucher() {
        String msg = "success";
        try {
            Boolean isExtract = isExtract("商品进货单");
            //单据未抽取才能进行单据抽取
            if(isExtract == false) {
                //日期相关数据
                DateInfo date = DateUtil.dateData();
                List<Tvoucher> tVoucherList = goodsIntoReturnService.goodsReceiptVoucher(date);
                List<TvoucherEntry> tVoucherEntryList = goodsIntoReturnService.goodsReceiptVoucherEntry(date);
                goodsIntoReturnService.insertGoodsReceiptVoucher(tVoucherList,tVoucherEntryList);
                isExtractService.changeExtractStatus("商品进货单");
            } else {
                msg = "本月商品进货单据已经抽取完成,不能重复抽取";
            }
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
        Boolean isExtract = isExtract("商品退货单");
        try {
            //单据未抽取才能进行单据抽取
            if(isExtract == false) {
                //日期相关数据
                DateInfo date = DateUtil.dateData();
                List<Tvoucher> tVoucherList = goodsIntoReturnService.goodsReturnReceiptVoucher(date);
                List<TvoucherEntry> tVoucherEntryList = goodsIntoReturnService.goodsReturnVoucherEntry(date);
                goodsIntoReturnService.insertGoodsReturnReceiptVoucher(tVoucherList,tVoucherEntryList);
                isExtractService.changeExtractStatus("商品退货单");
            } else {
                msg = "本月商品退货单据已经抽取完成,不能重复抽取";
            }
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
        Boolean isExtract = isExtract("商品移仓单");
        try {
            //单据未抽取才能进行单据抽取
            if(isExtract == false) {
                //日期相关数据
                DateInfo date = DateUtil.dateData();
                List<Tvoucher> tVoucherList = merchandiseShiftService.merchandiseShiftVoucher(date);
                List<TvoucherEntry> tVoucherEntryList = merchandiseShiftService.merchandiseShiftVoucherEntry(date);
                merchandiseShiftService.insertMerchandiseShiftVoucher(tVoucherList,tVoucherEntryList);
                isExtractService.changeExtractStatus("商品移仓单");
            } else {
                msg = "本月商品移仓单据已经抽取完成,不能重复抽取";
            }
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }

    /**
     * 抽取商店配货单单据
     * 每月25日定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 25 * ?")
    @RequestMapping("/productDistributionOrderVoucher")
    public String productDistributionOrderVoucher() {
        String msg = "success";
        Boolean isExtract = isExtract("商店配货单");
        try {
            //单据未抽取才能进行单据抽取
            if(isExtract == false) {
                //日期相关数据
                DateInfo date = DateUtil.dateData();
                List<Tvoucher> tVoucherList = productDistributionOrderService.productDistributionOrderVoucher(date);
                List<TvoucherEntry> tVoucherEntryList = productDistributionOrderService.productDistributionOrderVoucherEntry(date);
                productDistributionOrderService.insertProductDistributionOrderVoucher(tVoucherList,tVoucherEntryList);
                isExtractService.changeExtractStatus("商店配货单");
            } else {
                msg = "本月商店配货单单据已经抽取完成,不能重复抽取";
            }
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }

    /**
     * 抽取商店退货单单据
     * 每月25日定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 25 * ?")
    @RequestMapping("/storeReturnOrderVoucher")
    public String storeReturnOrderVoucher() {
        String msg = "success";
        Boolean isExtract = isExtract("商店退货单");
        try {
            //单据未抽取才能进行单据抽取
            if(isExtract == false) {
                //日期相关数据
                DateInfo date = DateUtil.dateData();
                List<Tvoucher> tVoucherList = productDistributionOrderService.storeReturnOrderVoucher(date);
                List<TvoucherEntry> tVoucherEntryList = productDistributionOrderService.storeReturnOrderVoucherEntry(date);
                productDistributionOrderService.insertStoreReturnOrderVoucher(tVoucherList,tVoucherEntryList);
                isExtractService.changeExtractStatus("商店退货单");
            } else {
                msg = "本月商店退货单单据已经抽取完成,不能重复抽取";
            }
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }

    /**
     * 判断本月该类单据是否抽取完成
     * @return
     */
    private Boolean isExtract(String type) {
        Boolean isExtract = false;
        try {
            //判断单据是否存在表中
            Object obj = isExtractService.isExtract(type);
            if(obj != null) {
                Map<String,String> map = (Map<String,String>)obj;
                String str = map.get("isExtract");
                if(str.trim().equals("1")) {
                    isExtract = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExtract;
    }
}
