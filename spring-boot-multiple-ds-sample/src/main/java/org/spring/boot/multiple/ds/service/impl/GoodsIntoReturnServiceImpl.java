package org.spring.boot.multiple.ds.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.boot.multiple.ds.TargetDataSource;
import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;
import org.spring.boot.multiple.ds.bean.YmlProp;
import org.spring.boot.multiple.ds.dao.GoodsIntoReturnBSDao;
import org.spring.boot.multiple.ds.dao.GoodsIntoReturnKingDeeDao;
import org.spring.boot.multiple.ds.service.GoodsIntoReturnService;
import org.spring.boot.multiple.ds.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author 刘世杰
 * @date 2019/8/14
 * 商品进退货impl
 */
@SuppressWarnings("unchecked")
@Service
public class GoodsIntoReturnServiceImpl implements GoodsIntoReturnService {

    @Autowired
    GoodsIntoReturnKingDeeDao goodsIntoReturnKingDeeDao;

    @Autowired
    GoodsIntoReturnBSDao goodsIntoReturnBsDao;

    @Autowired
    YmlProp ymlProp;

    /**
     * SLF4J
     */
    Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 查询凭证表所需数据-from:百盛
     * @param date
     */
    @TargetDataSource("dataSource")
    @Override
    public List<Tvoucher> goodsReceiptVoucher(DateInfo date) {
        log.info("进入goodsReceiptVoucher方法,查询百盛商品进货单数据处理并生成单据集合");
        //百盛
        List<Object> list = goodsIntoReturnBsDao.generatePurchase(date);
        DateInfo dateInfo = DateUtil.dateData();
        //凭证日期-当前时间
        String rq = dateInfo.getCurrentTime();
        List<Tvoucher> tVoucherList = new ArrayList<>();
        //插入tVoucher凭证
        for (Object obj : list) {
            Map<String,Object> map = (Map<String, Object>)obj;
            Tvoucher voucher = new Tvoucher();
            //凭证日期
            voucher.setFDate(rq);
            //业务日期
            voucher.setFTransDate(rq);
            //年份
            voucher.setFYear(dateInfo.getCurrentYear());
            //会计期间
            voucher.setFPeriod(dateInfo.getCurrentMonth());
            //摘要组内码
            voucher.setFGroupID("1");
            //摘要编码
            voucher.setFNumber("44");
            //参考信息
            voucher.setFReference(null);
            //摘要
            voucher.setFExplanation("");
            //附件张数
            voucher.setFAttachments("0");
            //分录数
            voucher.setFEntryCount(String.valueOf(map.get("TOTAL")));
            //借方金额合计
            voucher.setFDebitTotal(String.valueOf(map.get("SJJE")));
            //贷方金额合计
            voucher.setFCreditTotal(String.valueOf(map.get("SJJE")));
            //空-手工凭证,非空-机制凭证
            voucher.setFInternalInd(null);
            //0-未审核,1-已审核
            voucher.setFChecked("0");
            //0-未过账,1-已过账
            voucher.setFPosted("0");
            //制单人
            voucher.setFPreparerID("16394");
            //审核人
            voucher.setFCheckerID("-1");
            //记账人
            voucher.setFPosterID("-1");
            //出纳员
            voucher.setFCashierID("-1");
            //会计主管
            voucher.setFHandler(null);
            //其他系统传入凭证对象接口描述
            voucher.setFObjectName(null);
            //接口参数
            voucher.setFParameter(null);
            //凭证序号
            voucher.setFSerialNum("183");
            //单据类型
            voucher.setFTranType("0");
            //制单人所属工作组
            voucher.setFOwnerGroupID("0");

            //仓库名称
            voucher.setCKMC((String)map.get("CKMC"));
            //返回获取到的数据到金蝶
            tVoucherList.add(voucher);
        }
        return tVoucherList;
    }

    /**
     * 查询凭证表分录所需数据-from:百盛
     * @return
     * @param date
     */
    @TargetDataSource("dataSource")
    @Override
    public List<TvoucherEntry> goodsReceiptVoucherEntry(DateInfo date) {
        log.info("进入goodsReceiptVoucherEntry方法,查询百盛商品进货单数据处理并生成单据分录集合");
        List<Object> list = goodsIntoReturnBsDao.selectEntry(date);
        List<TvoucherEntry> tVoucherEntryList = new ArrayList<>();
        for(Object obj : list) {
            TvoucherEntry tvoucherEntry = new TvoucherEntry();
            Map<String,Object> map = (Map<String, Object>)obj;
            //摘要
            tvoucherEntry.setFExplanation("");
            //币别
            tvoucherEntry.setFCurrencyID("1");
            //费率类型
            tvoucherEntry.setFExchangeRateType("1");
            //费率
            tvoucherEntry.setFExchangeRate("1");
            //余额方向 0-贷方,1- 借方
            tvoucherEntry.setFDC("1");
            //金额（原币）
            tvoucherEntry.setFAmountFor(String.valueOf(map.get("F6")));
            //金额(本位币)
            tvoucherEntry.setFAmount(String.valueOf(map.get("F6")));
            //数量
            tvoucherEntry.setFQuantity("0");
            //单位内码
            tvoucherEntry.setFMeasureUnitID("0");
            //单价
            tvoucherEntry.setFUnitPrice("0");
            //机制凭证
            tvoucherEntry.setFInternalInd(null);
            //结算方式
            tvoucherEntry.setFSettleTypeID("0");
            //结算号
            tvoucherEntry.setFSettleNo(null);
            //现金流量 0-不是,1-是现金流量项目
            tvoucherEntry.setFCashFlowItem("0");
            //项目任务内码
            tvoucherEntry.setFTaskID("0");
            //项目资源ID
            tvoucherEntry.setFResourceID("0");
            //业务号
            tvoucherEntry.setFTransNo(null);

            //仓库名称
            tvoucherEntry.setCKMC((String)map.get("CKMC"));
            //供货商名称
            tvoucherEntry.setGHSMC((String)map.get("GHSMC"));
            tVoucherEntryList.add(tvoucherEntry);
        }
        return tVoucherEntryList;
    }

    /**
     * 添加凭证数据到金蝶系统
     * @param tVoucherList
     * @param tVoucherEntryList
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    @TargetDataSource("ds1")
    @Override
    public int insertGoodsReceiptVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList) {
        log.info("进入goodsReceiptVoucherEntry方法,插入商品进货单数据到金蝶凭证");
        for(Tvoucher tvoucher  : tVoucherList) {
            //查询最后插入的数据-没有自增主键,只能这么获取了
            Tvoucher obj = goodsIntoReturnKingDeeDao.selectLastVoucher();
            String FVoucherID;
            if(obj == null) {
                FVoucherID = "0";
            } else {
                FVoucherID = String.valueOf(Integer.parseInt(obj.getFVoucherID())+1);
            }
            tvoucher.setFVoucherID(FVoucherID);
            tvoucher.setFNumber(FVoucherID);
            tvoucher.setFSerialNum(FVoucherID);

            //插入凭证数据到金蝶系统
            goodsIntoReturnKingDeeDao.insertPurchase(tvoucher);
            //用于生成分录号
            int i = 0;

            //贷方数据
            TvoucherEntry tvoucherEntryTotal = new TvoucherEntry();
            //根据仓库名称查询id,放入核算项目
            Map<String,Object> ckId = (Map<String, Object>) goodsIntoReturnKingDeeDao.selectFNumber(tvoucher.getCKMC());
            if(ckId == null || ckId.isEmpty()) {
                //核算项目
                tvoucherEntryTotal.setFDetailID("0");
            } else {
                //核算项目
                tvoucherEntryTotal.setFDetailID(String.valueOf(ckId.get("FItemID")));
            }
            //摘要
            tvoucherEntryTotal.setFExplanation("");
            //币别
            tvoucherEntryTotal.setFCurrencyID("1");
            //费率类型
            tvoucherEntryTotal.setFExchangeRateType("1");
            //费率
            tvoucherEntryTotal.setFExchangeRate("1");
            //余额方向 0-贷方,1- 借方
            tvoucherEntryTotal.setFDC("0");
            //金额（原币）
            tvoucherEntryTotal.setFAmountFor(tvoucher.getFDebitTotal());
            //金额(本位币)
            tvoucherEntryTotal.setFAmount(tvoucher.getFDebitTotal());
            //数量
            tvoucherEntryTotal.setFQuantity("0");
            //单位内码
            tvoucherEntryTotal.setFMeasureUnitID("0");
            //单价
            tvoucherEntryTotal.setFUnitPrice("0");
            //机制凭证
            tvoucherEntryTotal.setFInternalInd(null);
            //结算方式
            tvoucherEntryTotal.setFSettleTypeID("0");
            //结算号
            tvoucherEntryTotal.setFSettleNo(null);
            //现金流量 0-不是,1-是现金流量项目
            tvoucherEntryTotal.setFCashFlowItem("0");
            //项目任务内码
            tvoucherEntryTotal.setFTaskID("0");
            //项目资源ID
            tvoucherEntryTotal.setFResourceID("0");
            //业务号
            tvoucherEntryTotal.setFTransNo(null);
            //仓库名称
            tvoucherEntryTotal.setCKMC(tvoucher.getCKMC());
            //科目内码
            tvoucherEntryTotal.setFAccountID(ymlProp.getSubjectAccountsPayable());
            //对方科目
            tvoucherEntryTotal.setFAccountID2(ymlProp.getSubjectStockGoods());
            //凭证内码
            tvoucherEntryTotal.setFVoucherID(FVoucherID);
            //分录号
            tvoucherEntryTotal.setFEntryID(String.valueOf(i));
            goodsIntoReturnKingDeeDao.insertVoucherEntry(tvoucherEntryTotal);
            i++;
            for(TvoucherEntry tVoucherEntry : tVoucherEntryList) {
                if(tvoucher.getCKMC().equals(tVoucherEntry.getCKMC())) {
                    //根据供货商名称查询id,放入核算项目
                    Map<String,Object> ghsId = (Map<String, Object>) goodsIntoReturnKingDeeDao.selectFNumber(tVoucherEntry.getGHSMC());
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
                    tVoucherEntry.setFAccountID2(ymlProp.getSubjectAccountsPayable());
                    //凭证内码
                    tVoucherEntry.setFVoucherID(FVoucherID);
                    //分录号
                    tVoucherEntry.setFEntryID(String.valueOf(i));
                    goodsIntoReturnKingDeeDao.insertVoucherEntry(tVoucherEntry);
                    i++;
                }
            }
        }
        return 0;
    }

    /**
     * 查询凭证表所需数据-from:百盛
     * @param date
     */
    @TargetDataSource("dataSource")
    @Override
    public List<Tvoucher> goodsReturnReceiptVoucher(DateInfo date) {
        log.info("进入goodsReturnReceiptVoucher方法,查询百盛商品退货单数据处理并生成单据集合");
        //百盛--商品退货单
        List<Object> list = goodsIntoReturnBsDao.returnGoodsVoucher(date);
        Calendar cal = Calendar.getInstance();
        List<Tvoucher> tVoucherList = new ArrayList<>();
        DateInfo dateInfo = DateUtil.dateData();
        //插入tVoucher凭证
        for (Object obj : list) {
            Map<String,Object> map = (Map<String, Object>)obj;
            Tvoucher voucher = new Tvoucher();
            //获取当前时间
            String rq = dateInfo.getCurrentTime();
            //凭证日期
            voucher.setFDate(rq);
            //业务日期
            voucher.setFTransDate(rq);
            //年份
            voucher.setFYear(dateInfo.getCurrentYear());
            //会计期间
            voucher.setFPeriod(dateInfo.getCurrentMonth());
            //摘要组内码
            voucher.setFGroupID("1");
            //摘要编码
            voucher.setFNumber("44");
            //参考信息
            voucher.setFReference(null);
            //摘要
            voucher.setFExplanation("");
            //附件张数
            voucher.setFAttachments("0");
            //分录数
            voucher.setFEntryCount(String.valueOf(map.get("TOTAL")));
            //借方金额合计
            voucher.setFDebitTotal(String.valueOf(map.get("SJJE")));
            //贷方金额合计
            voucher.setFCreditTotal(String.valueOf(map.get("SJJE")));
            //空-手工凭证,非空-机制凭证
            voucher.setFInternalInd(null);
            //0-未审核,1-已审核
            voucher.setFChecked("0");
            //0-未过账,1-已过账
            voucher.setFPosted("0");
            //制单人
            voucher.setFPreparerID("16394");
            //审核人
            voucher.setFCheckerID("-1");
            //记账人
            voucher.setFPosterID("-1");
            //出纳员
            voucher.setFCashierID("-1");
            //会计主管
            voucher.setFHandler(null);
            //其他系统传入凭证对象接口描述
            voucher.setFObjectName(null);
            //接口参数
            voucher.setFParameter(null);
            //凭证序号
            voucher.setFSerialNum("183");
            //单据类型
            voucher.setFTranType("0");
            //制单人所属工作组
            voucher.setFOwnerGroupID("0");

            //仓库名称
            voucher.setCKMC((String)map.get("CKMC"));
            //返回获取到的数据到金蝶
            tVoucherList.add(voucher);
        }
        return tVoucherList;
    }

    /**
     * 查询凭证表分录所需数据-from:百盛
     * @param date
     * @return
     */
    @TargetDataSource("dataSource")
    @Override
    public List<TvoucherEntry> goodsReturnVoucherEntry(DateInfo date) {
        log.info("进入goodsReturnVoucherEntry方法,查询百盛商品退货单数据处理并生成单据分录集合");
        List<Object> list = goodsIntoReturnBsDao.returnGoodsVoucherEntry(date);
        List<TvoucherEntry> tVoucherEntryList = new ArrayList<>();
        for(Object obj : list) {
            TvoucherEntry tvoucherEntry = new TvoucherEntry();
            Map<String,Object> map = (Map<String, Object>)obj;
            //摘要
            tvoucherEntry.setFExplanation("");
            //币别
            tvoucherEntry.setFCurrencyID("1");
            //费率类型
            tvoucherEntry.setFExchangeRateType("1");
            //费率
            tvoucherEntry.setFExchangeRate("1");
            //余额方向 0-贷方,1- 借方
            tvoucherEntry.setFDC("0");
            //金额（原币）
            tvoucherEntry.setFAmountFor(String.valueOf(map.get("F6")));
            //金额(本位币)
            tvoucherEntry.setFAmount(String.valueOf(map.get("F6")));
            //数量
            tvoucherEntry.setFQuantity("0");
            //单位内码
            tvoucherEntry.setFMeasureUnitID("0");
            //单价
            tvoucherEntry.setFUnitPrice("0");
            //机制凭证
            tvoucherEntry.setFInternalInd(null);
            //结算方式
            tvoucherEntry.setFSettleTypeID("0");
            //结算号
            tvoucherEntry.setFSettleNo(null);
            //现金流量 0-不是,1-是现金流量项目
            tvoucherEntry.setFCashFlowItem("0");
            //项目任务内码
            tvoucherEntry.setFTaskID("0");
            //项目资源ID
            tvoucherEntry.setFResourceID("0");
            //业务号
            tvoucherEntry.setFTransNo(null);

            //仓库名称
            tvoucherEntry.setCKMC((String)map.get("CKMC"));
            //供货商名称
            tvoucherEntry.setGHSMC((String)map.get("GHSMC"));
            tVoucherEntryList.add(tvoucherEntry);
        }
        return tVoucherEntryList;
    }

    /**
     * 添加凭证数据到金蝶系统-商品退货单
     * @param tVoucherList
     * @param tVoucherEntryList
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    @TargetDataSource("ds1")
    @Override
    public void insertGoodsReturnReceiptVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList) {
        log.info("进入insertGoodsReturnReceiptVoucher方法,插入商品退货单数据到金蝶凭证");
        for(Tvoucher tvoucher  : tVoucherList) {
            //查询最后插入的数据-没有自增主键,只能这么获取了
            Tvoucher obj = goodsIntoReturnKingDeeDao.selectLastVoucher();
            String FVoucherID;
            if(obj == null) {
                FVoucherID = "0";
            } else {
                FVoucherID = String.valueOf(Integer.parseInt(obj.getFVoucherID())+1);
            }
            tvoucher.setFVoucherID(FVoucherID);
            tvoucher.setFNumber(FVoucherID);
            tvoucher.setFSerialNum(FVoucherID);

            //插入凭证数据到金蝶系统
            goodsIntoReturnKingDeeDao.insertPurchase(tvoucher);
            //更新刚才插入的凭证数据-折中方案,无法获取自增值,只能进行修改了
            //用于生成分录号
            int i = 0;

            //贷方数据
            TvoucherEntry tvoucherEntryTotal = new TvoucherEntry();
            //根据仓库名称查询id,放入核算项目
            Map<String,Object> ckId = (Map<String, Object>) goodsIntoReturnKingDeeDao.selectFNumber(tvoucher.getCKMC());
            if(ckId == null || ckId.isEmpty()) {
                //核算项目
                tvoucherEntryTotal.setFDetailID("0");
            } else {
                //核算项目
                tvoucherEntryTotal.setFDetailID(String.valueOf(ckId.get("FItemID")));
            }
            //摘要
            tvoucherEntryTotal.setFExplanation("");
            //币别
            tvoucherEntryTotal.setFCurrencyID("1");
            //费率类型
            tvoucherEntryTotal.setFExchangeRateType("1");
            //费率
            tvoucherEntryTotal.setFExchangeRate("1");
            //余额方向 0-贷方,1- 借方
            tvoucherEntryTotal.setFDC("1");
            //金额（原币）
            tvoucherEntryTotal.setFAmountFor(tvoucher.getFDebitTotal());
            //金额(本位币)
            tvoucherEntryTotal.setFAmount(tvoucher.getFDebitTotal());
            //数量
            tvoucherEntryTotal.setFQuantity("0");
            //单位内码
            tvoucherEntryTotal.setFMeasureUnitID("0");
            //单价
            tvoucherEntryTotal.setFUnitPrice("0");
            //机制凭证
            tvoucherEntryTotal.setFInternalInd(null);
            //结算方式
            tvoucherEntryTotal.setFSettleTypeID("0");
            //结算号
            tvoucherEntryTotal.setFSettleNo(null);
            //现金流量 0-不是,1-是现金流量项目
            tvoucherEntryTotal.setFCashFlowItem("0");
            //项目任务内码
            tvoucherEntryTotal.setFTaskID("0");
            //项目资源ID
            tvoucherEntryTotal.setFResourceID("0");
            //业务号
            tvoucherEntryTotal.setFTransNo(null);
            //仓库名称
            tvoucherEntryTotal.setCKMC(tvoucher.getCKMC());
            //科目内码
            tvoucherEntryTotal.setFAccountID(ymlProp.getSubjectAccountsPayable());
            //对方科目
            tvoucherEntryTotal.setFAccountID2(ymlProp.getSubjectStockGoods());
            //凭证内码
            tvoucherEntryTotal.setFVoucherID(FVoucherID);
            //分录号
            tvoucherEntryTotal.setFEntryID(String.valueOf(i));
            goodsIntoReturnKingDeeDao.insertVoucherEntry(tvoucherEntryTotal);
            i++;
            for(TvoucherEntry tVoucherEntry : tVoucherEntryList) {
                if(tvoucher.getCKMC().equals(tVoucherEntry.getCKMC())) {
                    //根据供货商名称查询id,放入核算项目
                    Map<String,Object> ghsId = (Map<String, Object>) goodsIntoReturnKingDeeDao.selectFNumber(tVoucherEntry.getGHSMC());
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
                    tVoucherEntry.setFAccountID2(ymlProp.getSubjectAccountsPayable());
                    //凭证内码
                    tVoucherEntry.setFVoucherID(FVoucherID);
                    //分录号
                    tVoucherEntry.setFEntryID(String.valueOf(i));
                    goodsIntoReturnKingDeeDao.insertVoucherEntry(tVoucherEntry);
                    i++;
                }
            }
        }
    }
}