package org.spring.boot.multiple.ds.controller;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;
import org.spring.boot.multiple.ds.service.*;
import org.spring.boot.multiple.ds.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 刘世杰
 * @EnableScheduling 开启定时任务
 * {@link #goodsReceiptVoucher}:商品进货单据
 * {@link #goodsReturnReceipt}:商品退货单据
 * {@link #merchandiseShiftVoucher}:商品移仓单据
 * {@link #productDistributionOrderVoucher}:商店配货单据
 * {@link #storeReturnOrderVoucher}:商店退货单据
 * {@link #incomingStatement}:进货结算单
 * <em>
 * 商场销售抽取时间段: [上个月26日,本月25日]
 * 商场销售外抽取时间段: [上个月1日,上个月最后1日]
 * </em>
 */
@SuppressWarnings("unchecked")
@EnableScheduling
@RestController
public class YueTuController {

    @Autowired
    private GoodsIntoReturnService goodsIntoReturnService;

    @Autowired
    private MerchandiseShiftService merchandiseShiftService;

    @Autowired
    private ProductDistributionOrderService productDistributionOrderService;

    @Autowired
    private IsExtractService isExtractService;

    @Autowired
    private incomingStatementService incomingStatementService;

    /**
     * default msg
     */
    private static String msg = "SUCCESS";

    /**
     * 抽取商品进货单据
     * 每月1日0时定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @RequestMapping("/goodsReceiptVoucher")
    public String goodsReceiptVoucher() {
        try {
            Boolean isExtract = isExtract("商品进货单");
            //单据未抽取才能进行单据抽取
            if(!isExtract) {
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
     * 每月1日0时定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @RequestMapping("/goodsReturnReceiptVoucher")
    public String goodsReturnReceipt() {
        Boolean isExtract = isExtract("商品退货单");
        try {
            //单据未抽取才能进行单据抽取
            if(!isExtract) {
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
     * 抽取商品移仓单单据--移入
     * 每月1日0时定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @RequestMapping("/merchandiseShiftVoucher")
    public String merchandiseShiftVoucher() {
        Boolean isExtract = isExtract("商品移仓单");
        try {
            //单据未抽取才能进行单据抽取
            if(!isExtract) {
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
     * 抽取商品移仓单单据--移出
     * 每月1日0时定期执行-cron表达式
     */
    @SuppressWarnings(value = "all")
    @Scheduled(cron = "0 0 0 1 * ?")
    @RequestMapping("/merchandiseShiftOutVoucher")
    public String merchandiseShiftOutVoucher() {
        String msg = "success";
        Boolean isExtract = isExtract("商品移仓单");
        try {
            //单据未抽取才能进行单据抽取
            if(!isExtract) {
                //日期相关数据
                DateInfo date = DateUtil.dateData();
                List<Tvoucher> tVoucherList = merchandiseShiftService.merchandiseShiftOutVoucher(date);
                List<TvoucherEntry> tVoucherEntryList = merchandiseShiftService.merchandiseShiftOutVoucherEntry(date);
                merchandiseShiftService.insertMerchandiseShiftVoucher(tVoucherList,tVoucherEntryList);
                isExtractService.changeExtractStatus("商品移仓单移出");
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
     * 每月1日定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @RequestMapping("/productDistributionOrderVoucher")
    public String productDistributionOrderVoucher() {
        Boolean isExtract = isExtract("商店配货单");
        try {
            //单据未抽取才能进行单据抽取
            if(!isExtract) {
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
     * 每月1日0时定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @RequestMapping("/storeReturnOrderVoucher")
    public String storeReturnOrderVoucher() {
        Boolean isExtract = isExtract("商店退货单");
        try {
            //单据未抽取才能进行单据抽取
            if(!isExtract) {
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
     * 抽取进货结算单
     * 每月1日0时定期执行-cron表达式
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @RequestMapping("/incomingStatement")
    public String incomingStatement() {
        Boolean isExtract = isExtract("进货结算单");
        try {
            //日期相关数据
            DateInfo date = DateUtil.dateData();
            Map<String, List<Object>> map = incomingStatementService.incomingStatementVoucher(date);
            List<Tvoucher> tvoucherList = new ArrayList<>();
            List<TvoucherEntry> tvoucherEntryList = new ArrayList<>();
            List<Object> list01 = map.get("voucher");
            List<Object> list02 = map.get("voucherEntry");
            for (Object obj : list01) {
                tvoucherList = (List<Tvoucher>) obj;
            }
            for(Object obj : list02) {
                tvoucherEntryList = (List<TvoucherEntry>)obj;
            }
            incomingStatementService.insertIncomingStatementVoucher(tvoucherList,tvoucherEntryList);
        } catch (Exception e) {
            e.getMessage();
        }
        return msg;
    }

    /**
     * 判断本月该类单据是否抽取完成
     *
     * <strong>"1":本次单据已经抽取;"0":本次单据未抽取</strong>
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
                if("1".equals(str.trim())) {
                    isExtract = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExtract;
    }
}
