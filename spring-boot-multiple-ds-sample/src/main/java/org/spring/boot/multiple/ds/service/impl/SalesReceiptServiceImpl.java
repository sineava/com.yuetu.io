package org.spring.boot.multiple.ds.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.boot.multiple.ds.TargetDataSource;
import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;
import org.spring.boot.multiple.ds.bean.YmlProp;
import org.spring.boot.multiple.ds.dao.SalesReceiptBSDao;
import org.spring.boot.multiple.ds.dao.SalesReceiptKingDeeDao;
import org.spring.boot.multiple.ds.service.SalesReceiptService;
import org.spring.boot.multiple.ds.util.VoucherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author 刘世杰
 * @date 2019/9/20
 * 销货收款单impl
 */
@SuppressWarnings("unchecked")
@Service
public class SalesReceiptServiceImpl implements SalesReceiptService {

    @Autowired
    SalesReceiptBSDao salesReceiptBSDao;

    @Autowired
    SalesReceiptKingDeeDao salesReceiptKingDeeDao;

    @Autowired
    YmlProp ymlProp;

    /**
     * SLF4J
     */
    Logger log = LoggerFactory.getLogger(getClass());

    @TargetDataSource("dataSource")
    @Override
    public Map<String, List<Object>> salesReceiptVoucher(DateInfo date) throws Exception {
        log.info("进入salesReceiptVoucher方法,查询百盛销货收款单数据处理并生成单据以及分录集合");
        //凭证集合
        List<Tvoucher> tvoucherList = new ArrayList<>();
        //凭证分录集合
        List<TvoucherEntry> tvoucherEntryList = new ArrayList<>();
        //查询凭证所需数据
        List<Object> list = salesReceiptBSDao.getVoucher(date);
        for(Object obj : list) {
            if(!ObjectUtils.isEmpty(obj)) {
                Map<String,Object> map = (Map<String, Object>)obj;
                Tvoucher voucher = VoucherUtil.getVoucher();
                //凭证日期
                voucher.setFDate(date.getCurrentTime());
                //业务日期
                voucher.setFTransDate(date.getCurrentTime());
                //年份
                voucher.setFYear(date.getCurrentYear());
                //会计期间
                voucher.setFPeriod(date.getCurrentMonth());
                //摘要
                voucher.setFExplanation(String.valueOf(map.get("BZ")));
                //借方金额合计
                voucher.setFDebitTotal(String.valueOf(map.get("SJJE")));
                //贷方金额合计
                voucher.setFCreditTotal(String.valueOf(map.get("SJJE")));
                //收款方式
                voucher.setSkfs(String.valueOf(map.get("DM2")));
                /**获取单据编号*/
                String djbh = String.valueOf(map.get("DJBH"));
                /**单据编号:用于数据关联*/
                voucher.setDjbh(djbh);
                //客户代码
                String dm1 = String.valueOf(map.get("DM1"));
                Map<String,Object> khMap = (Map<String,Object>)salesReceiptBSDao.getKHMC(dm1);
                String khmc = String.valueOf(khMap.get("KHMC"));
                voucher.setKhmc(khmc);
                //用于生成分录号
                int i = 0;
                /**查询凭证分录*/
                List<Object> ls = salesReceiptBSDao.getVoucherEntry(djbh);
                voucher.setFEntryCount(String.valueOf(ls.size()+1));
                //汇总数据
                TvoucherEntry tvoucherEntryTotal = VoucherUtil.getVoucherEntry();
                //摘要
                tvoucherEntryTotal.setFExplanation("");
                //金额（原币）
                tvoucherEntryTotal.setFAmountFor(voucher.getFDebitTotal());
                //金额(本位币)
                tvoucherEntryTotal.setFAmount(voucher.getFDebitTotal());
                //客户名称
                tvoucherEntryTotal.setKhmc(khmc);
                //分录号
                tvoucherEntryTotal.setFEntryID(String.valueOf(i));
                /**单据编号:用于数据关联*/
                tvoucherEntryTotal.setDjbh(djbh);
                //余额方向 0-贷方,1- 借方
                tvoucherEntryTotal.setFDC("0");
                /**设置是否为汇总数据 1-是*/
                tvoucherEntryTotal.setIsSummary("1");
                //根据收款账号查询收款名称
                for(Object objEntry : ls) {
                    if(!ObjectUtils.isEmpty(objEntry)) {
                        Map<String, Object> mapEntry = (Map<String, Object>) objEntry;
                        String skzh = String.valueOf(mapEntry.get("SKZH"));
                        //根据收款账号查询收款账户名
                        Object zhmc = salesReceiptBSDao.getZHMC(skzh);
                        TvoucherEntry tvoucherEntry = VoucherUtil.getVoucherEntry();
                        //摘要
                        tvoucherEntry.setFExplanation("");
                        //金额（原币）
                        tvoucherEntry.setFAmountFor(String.valueOf(mapEntry.get("SKJE")));
                        //金额(本位币)
                        tvoucherEntry.setFAmount(String.valueOf(mapEntry.get("SKJE")));
                        //分录号
                        tvoucherEntry.setFEntryID(String.valueOf(i));
                        //余额方向 0-贷方,1- 借方
                        tvoucherEntry.setFDC("1");
                        /**单据编号-用于数据关联*/
                        tvoucherEntry.setDjbh(djbh);
                        /**设置是否为汇总数据 0-否*/
                        tvoucherEntry.setIsSummary("0");
                        i++;
                        /**放入凭证分录集合*/
                        tvoucherEntryList.add(tvoucherEntry);
                    }
                }
                /**放入凭证分录集合*/
                tvoucherEntryList.add(tvoucherEntryTotal);
                /**放入凭证集合*/
                tvoucherList.add(voucher);
            }
        }
        Map<String, List<Object>> map = new HashMap<>();
        map.put("voucher", Collections.singletonList(tvoucherList));
        map.put("voucherEntry",Collections.singletonList(tvoucherEntryList));
        return map;
    }

    @Transactional(rollbackFor = {Exception.class})
    @TargetDataSource("ds1")
    @Override
    public void insertSalesReceiptVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList) {
        log.info("进入insertSalesReceiptVoucher方法,插入百盛销货收款单数据到金蝶凭证");
        for(Tvoucher tvoucher : tVoucherList) {
            Tvoucher obj = salesReceiptKingDeeDao.selectLastVoucher();
            String FVoucherID = "0";
            if(obj != null) {
                FVoucherID = String.valueOf(Integer.parseInt(obj.getFVoucherID())+1);
            }
            tvoucher.setFVoucherID(FVoucherID);
            tvoucher.setFNumber(FVoucherID);
            tvoucher.setFSerialNum(FVoucherID);
            //插入凭证数据到金蝶系统
            salesReceiptKingDeeDao.addVoucher(tvoucher);
        }
    }
}
