package org.spring.boot.multiple.ds.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.boot.multiple.ds.TargetDataSource;
import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;
import org.spring.boot.multiple.ds.bean.YmlProp;
import org.spring.boot.multiple.ds.dao.ProductDistributionOrderBSDao;
import org.spring.boot.multiple.ds.dao.ProductDistributionOrderKingDeeDao;
import org.spring.boot.multiple.ds.service.ProductDistributionOrderService;
import org.spring.boot.multiple.ds.util.DateUtil;
import org.spring.boot.multiple.ds.util.VoucherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 刘世杰
 * @date 2019/8/22
 * 商店配货退货单
 */
@SuppressWarnings("unchecked")
@Service
public class ProductDistributionOrderServiceImpl implements ProductDistributionOrderService {

    @Autowired
    ProductDistributionOrderBSDao productDistributionOrderBSDao;

    @Autowired
    ProductDistributionOrderKingDeeDao productDistributionOrderKingDeeDao;

    @Autowired
    YmlProp ymlProp;

    /**
     * SLF4J
     */
    Logger log = LoggerFactory.getLogger(getClass());

    @TargetDataSource("dataSource")
    @Override
    public List<Tvoucher> productDistributionOrderVoucher(DateInfo date) throws Exception {
        log.info("进入productDistributionOrderVoucher方法,查询百盛商店配货单数据处理并生成单据集合");
        List<Object> list = productDistributionOrderBSDao.getVoucher(date);
        DateInfo dateInfo = DateUtil.dateData();
        List<Tvoucher> tVoucherList = new ArrayList<>();
        for(Object obj : list) {
            if(!ObjectUtils.isEmpty(obj)) {
                Map<String,Object> map = (Map<String, Object>)obj;
                Tvoucher voucher = VoucherUtil.getVoucher();
                //凭证日期
                voucher.setFDate(dateInfo.getCurrentMonth25());
                //业务日期
                voucher.setFTransDate(dateInfo.getCurrentMonth25());
                //年份
                voucher.setFYear(dateInfo.getCurrentYear());
                //会计期间
                voucher.setFPeriod(dateInfo.getCurrentMonth());
                //摘要
                voucher.setFExplanation("摘要");
                //分录数
                voucher.setFEntryCount(String.valueOf(map.get("TOTAL")));
                //借方金额合计
                voucher.setFDebitTotal(String.valueOf(map.get("CBJE")));
                //贷方金额合计
                voucher.setFCreditTotal(String.valueOf(map.get("CBJE")));
                //贷方:CKMC
                voucher.setCredit(String.valueOf(map.get("CKMC")));
                //返回百盛获取到的数据
                tVoucherList.add(voucher);
            }
        }
        return tVoucherList;
    }

    @TargetDataSource("dataSource")
    @Override
    public List<TvoucherEntry> productDistributionOrderVoucherEntry(DateInfo date) throws Exception {
        log.info("进入productDistributionOrderVoucherEntry方法,查询百盛商店配货单数据处理并生成单据分录集合");
        List<Object> list = productDistributionOrderBSDao.getVoucherEntry(date);
        List<TvoucherEntry> tVoucherEntryList = new ArrayList<>();
        for(Object obj : list) {
            if(!ObjectUtils.isEmpty(obj)) {
                TvoucherEntry tvoucherEntry = VoucherUtil.getVoucherEntry();
                Map<String, Object> map = (Map<String, Object>) obj;
                //摘要
                tvoucherEntry.setFExplanation("摘要");
                //余额方向 0-贷方,1- 借方
                tvoucherEntry.setFDC("1");
                //金额（原币）
                tvoucherEntry.setFAmountFor(String.valueOf(map.get("CBJE")));
                //金额(本位币)
                tvoucherEntry.setFAmount(String.valueOf(map.get("CBJE")));
                //贷方:CKMC
                tvoucherEntry.setCredit((String)map.get("CKMC"));
                //借方:CKMC1
                tvoucherEntry.setDebit((String)map.get("CKMC1"));
                tVoucherEntryList.add(tvoucherEntry);
            }
        }
        return tVoucherEntryList;
    }

    @Transactional(rollbackFor = {Exception.class})
    @TargetDataSource("ds1")
    @Override
    public void insertProductDistributionOrderVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList) throws Exception {
        log.info("进入insertProductDistributionOrderVoucher方法,插入商品调仓单数据到金蝶凭证");
        for(Tvoucher tvoucher  : tVoucherList) {
            Tvoucher obj = productDistributionOrderKingDeeDao.selectLastVoucher();
            String FVoucherID = "0";
            if (obj != null) {
                FVoucherID = String.valueOf(Integer.parseInt(obj.getFVoucherID()) + 1);
            }
            tvoucher.setFVoucherID(FVoucherID);
            tvoucher.setFNumber(FVoucherID);
            tvoucher.setFSerialNum(FVoucherID);
            //插入凭证数据到金蝶系统
            productDistributionOrderKingDeeDao.addVoucher(tvoucher);
            //用于生成分录号
            int i = 0;
            //贷方数据
            TvoucherEntry tvoucherEntryTotal = VoucherUtil.getVoucherEntry();
            //根据仓库名称CKMC查询对应的FItemID
            Map<String,Object> fItemid = (Map<String, Object>) productDistributionOrderKingDeeDao.selectFItemID(tvoucher.getCredit());
            if(fItemid == null || fItemid.isEmpty()) {
                //核算项目
                tvoucherEntryTotal.setFDetailID("0");
            } else {
                //核算项目
                tvoucherEntryTotal.setFDetailID(String.valueOf(fItemid.get("FItemID")));
            }
            //摘要
            tvoucherEntryTotal.setFExplanation("摘要");
            //金额（原币）
            tvoucherEntryTotal.setFAmountFor(tvoucher.getFDebitTotal());
            //金额(本位币)
            tvoucherEntryTotal.setFAmount(tvoucher.getFDebitTotal());
            //贷方-CKMC1
            tvoucherEntryTotal.setCredit(tvoucher.getCredit());
            //科目内码
            tvoucherEntryTotal.setFAccountID(ymlProp.getSubjectStockGoods());
            //对方科目
            tvoucherEntryTotal.setFAccountID2(ymlProp.getSubjectStockGoods());
            //凭证内码
            tvoucherEntryTotal.setFVoucherID(FVoucherID);
            //分录号
            tvoucherEntryTotal.setFEntryID(String.valueOf(i));
            //余额方向 0-贷方,1- 借方
            tvoucherEntryTotal.setFDC("0");
            productDistributionOrderKingDeeDao.addVoucherEntry(tvoucherEntryTotal);
            i++;
            for(TvoucherEntry tVoucherEntry : tVoucherEntryList) {
                if(tvoucher.getCredit().equals(tVoucherEntry.getCredit())) {
                    //根据仓库名称CKMC查询对应的FItemID
                    Map<String,Object> ghsId = (Map<String, Object>) productDistributionOrderKingDeeDao.selectFItemID(tVoucherEntry.getDebit());
                    if(ghsId == null || ghsId.isEmpty()) {
                        //核算项目
                        tVoucherEntry.setFDetailID("0");
                    } else {
                        //核算项目
                        tVoucherEntry.setFDetailID(String.valueOf(ghsId.get("FItemID")));
                    }
                    //科目内码
                    tVoucherEntry.setFAccountID(ymlProp.getSubjectStockGoods());
                    //对方科目
                    tVoucherEntry.setFAccountID2(ymlProp.getSubjectStockGoods());
                    //凭证内码
                    tVoucherEntry.setFVoucherID(FVoucherID);
                    //分录号
                    tVoucherEntry.setFEntryID(String.valueOf(i));
                    productDistributionOrderKingDeeDao.addVoucherEntry(tVoucherEntry);
                    i++;
                }
            }
        }
    }

    @TargetDataSource("dataSource")
    @Override
    public List<Tvoucher> storeReturnOrderVoucher(DateInfo date) throws Exception {
        log.info("进入productDistributionOrderVoucher方法,查询百盛商店配货单数据处理并生成单据集合");
        List<Object> list = productDistributionOrderBSDao.getReturnVoucher(date);
        DateInfo dateInfo = DateUtil.dateData();
        List<Tvoucher> tVoucherList = new ArrayList<>();
        for(Object obj : list) {
            if(!ObjectUtils.isEmpty(obj)) {
                Map<String,Object> map = (Map<String, Object>)obj;
                Tvoucher voucher = VoucherUtil.getVoucher();
                //凭证日期
                voucher.setFDate(dateInfo.getCurrentMonth25());
                //业务日期
                voucher.setFTransDate(dateInfo.getCurrentMonth25());
                //年份
                voucher.setFYear(dateInfo.getCurrentYear());
                //会计期间
                voucher.setFPeriod(dateInfo.getCurrentMonth());
                //摘要
                voucher.setFExplanation("摘要");
                //分录数
                voucher.setFEntryCount(String.valueOf(map.get("TOTAL")));
                //借方金额合计
                voucher.setFDebitTotal(String.valueOf(map.get("CBJE")));
                //贷方金额合计
                voucher.setFCreditTotal(String.valueOf(map.get("CBJE")));
                //贷方:CKMC
                voucher.setCredit(String.valueOf(map.get("CKMC")));
                //返回百盛获取到的数据
                tVoucherList.add(voucher);
            }
        }
        return tVoucherList;
    }

    @TargetDataSource("dataSource")
    @Override
    public List<TvoucherEntry> storeReturnOrderVoucherEntry(DateInfo date) throws Exception {
        log.info("进入productDistributionOrderVoucherEntry方法,查询百盛商店配货单数据处理并生成单据分录集合");
        List<Object> list = productDistributionOrderBSDao.getReturnVoucherEntry(date);
        List<TvoucherEntry> tVoucherEntryList = new ArrayList<>();
        for(Object obj : list) {
            if(!ObjectUtils.isEmpty(obj)) {
                TvoucherEntry tvoucherEntry = VoucherUtil.getVoucherEntry();
                Map<String, Object> map = (Map<String, Object>) obj;
                //摘要
                tvoucherEntry.setFExplanation("摘要");
                //余额方向 0-贷方,1- 借方
                tvoucherEntry.setFDC("0");
                //金额（原币）
                tvoucherEntry.setFAmountFor(String.valueOf(map.get("CBJE")));
                //金额(本位币)
                tvoucherEntry.setFAmount(String.valueOf(map.get("CBJE")));
                //贷方:CKMC
                tvoucherEntry.setCredit((String)map.get("CKMC"));
                //借方:CKMC1
                tvoucherEntry.setDebit((String)map.get("CKMC1"));
                tVoucherEntryList.add(tvoucherEntry);
            }
        }
        return tVoucherEntryList;
    }

    @Transactional(rollbackFor = {Exception.class})
    @TargetDataSource("ds1")
    @Override
    public void insertStoreReturnOrderVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList) throws Exception {
        log.info("进入insertStoreReturnOrderVoucher方法,插入商店配货单数据到金蝶凭证");
        for(Tvoucher tvoucher  : tVoucherList) {
            Tvoucher obj = productDistributionOrderKingDeeDao.selectLastVoucher();
            String FVoucherID = "0";
            if (obj != null) {
                FVoucherID = String.valueOf(Integer.parseInt(obj.getFVoucherID()) + 1);
            }
            tvoucher.setFVoucherID(FVoucherID);
            tvoucher.setFNumber(FVoucherID);
            tvoucher.setFSerialNum(FVoucherID);
            //插入凭证数据到金蝶系统
            productDistributionOrderKingDeeDao.addVoucher(tvoucher);
            //用于生成分录号
            int i = 0;
            //贷方数据
            TvoucherEntry tvoucherEntryTotal = VoucherUtil.getVoucherEntry();
            //根据仓库名称CKMC查询对应的FItemID
            Map<String,Object> fItemid = (Map<String, Object>) productDistributionOrderKingDeeDao.selectFItemID(tvoucher.getCredit());
            if(fItemid == null || fItemid.isEmpty()) {
                //核算项目
                tvoucherEntryTotal.setFDetailID("0");
            } else {
                //核算项目
                tvoucherEntryTotal.setFDetailID(String.valueOf(fItemid.get("FItemID")));
            }
            //摘要
            tvoucherEntryTotal.setFExplanation("摘要");
            //金额（原币）
            tvoucherEntryTotal.setFAmountFor(tvoucher.getFDebitTotal());
            //金额(本位币)
            tvoucherEntryTotal.setFAmount(tvoucher.getFDebitTotal());
            //贷方-CKMC1
            tvoucherEntryTotal.setCredit(tvoucher.getCredit());
            //科目内码
            tvoucherEntryTotal.setFAccountID(ymlProp.getSubjectStockGoods());
            //对方科目
            tvoucherEntryTotal.setFAccountID2(ymlProp.getSubjectStockGoods());
            //凭证内码
            tvoucherEntryTotal.setFVoucherID(FVoucherID);
            //分录号
            tvoucherEntryTotal.setFEntryID(String.valueOf(i));
            //余额方向 0-贷方,1- 借方
            tvoucherEntryTotal.setFDC("1");
            productDistributionOrderKingDeeDao.addVoucherEntry(tvoucherEntryTotal);
            i++;
            for(TvoucherEntry tVoucherEntry : tVoucherEntryList) {
                if(tvoucher.getCredit().equals(tVoucherEntry.getCredit())) {
                    //根据仓库名称CKMC查询对应的FItemID
                    Map<String,Object> ghsId = (Map<String, Object>) productDistributionOrderKingDeeDao.selectFItemID(tVoucherEntry.getDebit());
                    if(ghsId == null || ghsId.isEmpty()) {
                        //核算项目
                        tVoucherEntry.setFDetailID("0");
                    } else {
                        //核算项目
                        tVoucherEntry.setFDetailID(String.valueOf(ghsId.get("FItemID")));
                    }
                    //科目内码
                    tVoucherEntry.setFAccountID(ymlProp.getSubjectStockGoods());
                    //对方科目
                    tVoucherEntry.setFAccountID2(ymlProp.getSubjectStockGoods());
                    //凭证内码
                    tVoucherEntry.setFVoucherID(FVoucherID);
                    //分录号
                    tVoucherEntry.setFEntryID(String.valueOf(i));
                    productDistributionOrderKingDeeDao.addVoucherEntry(tVoucherEntry);
                    i++;
                }
            }
        }
    }
}
