package org.spring.boot.multiple.ds.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.boot.multiple.ds.TargetDataSource;
import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;
import org.spring.boot.multiple.ds.bean.YmlProp;
import org.spring.boot.multiple.ds.dao.IncomingStatementBSDao;
import org.spring.boot.multiple.ds.dao.IncomingStatementKingDeeDao;
import org.spring.boot.multiple.ds.service.incomingStatementService;
import org.spring.boot.multiple.ds.util.VoucherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author 刘世杰
 * @date 2019/8/22
 * 进货结算单
 */
@SuppressWarnings("unchecked")
@Service
public class IncomingStatementServiceImpl implements incomingStatementService {

    @Autowired
    IncomingStatementBSDao incomingStatementBSDao;

    @Autowired
    IncomingStatementKingDeeDao incomingStatementKingDeeDao;

    @Autowired
    YmlProp ymlProp;

    /**
     * SLF4J
     */
    Logger log = LoggerFactory.getLogger(getClass());

    @TargetDataSource("dataSource")
    @Override
    public Map<String, List<Object>> incomingStatementVoucher(DateInfo date) throws Exception {
        log.info("进入storeReturnOrderVoucher方法,查询百盛进货结算单数据处理并生成单据以及分录集合");
        //凭证集合
        List<Tvoucher> tvoucherList = new ArrayList<>();
        //凭证分录集合
        List<TvoucherEntry> tvoucherEntryList = new ArrayList<>();
        //查询凭证所需数据
        List<Object> list = incomingStatementBSDao.getVoucher(date);
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
                voucher.setFDebitTotal(String.valueOf(map.get("JE")));
                //贷方金额合计
                voucher.setFCreditTotal(String.valueOf(map.get("JE")));
                /**单据编号*/
                String DJBH = String.valueOf(map.get("DJBH"));
                /***根据DM1(供货商代码)查询对应的供货商名称--两个系统只能根据名称匹配(编号并不一致)***/
                String DM1 = String.valueOf(map.get("DM1"));
                Map<String,Object> ghsMap = (Map<String,Object>)incomingStatementBSDao.getGHSMC(DM1);
                //供货商名称
                String GHSMC = String.valueOf(ghsMap.get("GHSMC"));
                voucher.setGHSMC(GHSMC);
                /***根据DM2(仓库代码)查询对应的仓库名称--两个系统只能根据名称匹配(编号并不一致)***/
                String DM2 = String.valueOf(map.get("DM2"));
                Map<String,Object> ckMap = (Map<String,Object>)incomingStatementBSDao.getCKMC(DM2);
                String CKMC = String.valueOf(ckMap.get("CKMC"));
                voucher.setCKMC(CKMC);
                //根据单据编号DJBH查询对应的分录数据
                List<Object> ls = incomingStatementBSDao.getVoucherEntry(DJBH);
                /**分录数*/
                voucher.setFEntryCount(String.valueOf(ls.size()));
                /**单据编号-用于数据关联*/
                voucher.setDjbh(DJBH);
                //用于生成分录号
                int i = 0;
                //汇总数据
                TvoucherEntry tvoucherEntryTotal = VoucherUtil.getVoucherEntry();
                //摘要
                tvoucherEntryTotal.setFExplanation("");
                //金额（原币）
                tvoucherEntryTotal.setFAmountFor(voucher.getFDebitTotal());
                //金额(本位币)
                tvoucherEntryTotal.setFAmount(voucher.getFDebitTotal());
                //贷方-供货商
                tvoucherEntryTotal.setCredit(voucher.getGHSMC());
                //分录号
                tvoucherEntryTotal.setFEntryID(String.valueOf(i));
                /**设置是否为汇总数据 1-是*/
                tvoucherEntryTotal.setIsSummary("1");
                /**单据编号-用于数据关联*/
                tvoucherEntryTotal.setDjbh(DJBH);
                i++;
                //确定总数据金额方向
                String jefx = "";
                for(Object objEntry : ls) {
                    if(!ObjectUtils.isEmpty(objEntry)) {
                        TvoucherEntry tvoucherEntry = VoucherUtil.getVoucherEntry();
                        Map<String, Object> mapEntry = (Map<String, Object>) objEntry;
                        /**获取单据性质,在数据字典未找到该表,测试发现0金额是正数,1金额是负数;推测0是进货单,1是退货单**/
                        String djxz = String.valueOf(mapEntry.get("DJXZ"));
                        if("0".equals(djxz)) {
                            //余额方向 0-贷方,1- 借方
                            tvoucherEntry.setFDC("1");
                            jefx = "1";
                        } else if("1".equals(djxz)) {
                            //余额方向 0-贷方,1- 借方
                            tvoucherEntry.setFDC("0");
                            jefx = "0";
                        }
                        //摘要
                        tvoucherEntry.setFExplanation("");
                        //金额（原币）
                        tvoucherEntry.setFAmountFor(String.valueOf(mapEntry.get("JE")));
                        //金额(本位币)
                        tvoucherEntry.setFAmount(String.valueOf(mapEntry.get("JE")));
                        //仓库名称: CKMC
                        tvoucherEntry.setCKMC(CKMC);
                        //供货商名称：GHSMC
                        tvoucherEntry.setGHSMC(GHSMC);
                        //分录号
                        tvoucherEntry.setFEntryID(String.valueOf(i));
                        /**单据编号-用于数据关联*/
                        tvoucherEntry.setDjbh(DJBH);
                        /**设置是否为汇总数据 0-否*/
                        tvoucherEntry.setIsSummary("0");
                        i++;
                        /**放入凭证分录集合*/
                        tvoucherEntryList.add(tvoucherEntry);
                    }
                }
                if("0".equals(jefx)) {
                    //余额方向 0-贷方,1- 借方
                    tvoucherEntryTotal.setFDC("1");
                } else if("1".equals(jefx)) {
                    //余额方向 0-贷方,1- 借方
                    tvoucherEntryTotal.setFDC("0");
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
    public void insertIncomingStatementVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList) throws Exception {
        log.info("进入insertIncomingStatementVoucher方法,插入百盛进货结算单数据到金蝶凭证");
        for(Tvoucher tvoucher : tVoucherList) {
            Tvoucher obj = incomingStatementKingDeeDao.selectLastVoucher();
            String FVoucherID = "0";
            if(obj != null) {
                FVoucherID = String.valueOf(Integer.parseInt(obj.getFVoucherID())+1);
            }
            tvoucher.setFVoucherID(FVoucherID);
            tvoucher.setFNumber(FVoucherID);
            tvoucher.setFSerialNum(FVoucherID);
            //插入凭证数据到金蝶系统
            incomingStatementKingDeeDao.addVoucher(tvoucher);
            //根据供货商名称查询FItemID
            Map<String,Object> ghsFDetailID = (Map<String, Object>)incomingStatementKingDeeDao.selectDetailID(tvoucher.getGHSMC(),"8");
            //根据仓库名称查询FItemID
            Map<String,Object> ckFDetailId = (Map<String, Object>)incomingStatementKingDeeDao.selectDetailID(tvoucher.getCKMC(),"5");
            for(TvoucherEntry tVoucherEntry : tVoucherEntryList) {
                if(tVoucherEntry.getDjbh().equals(tvoucher.getDjbh())) {
                    if(tVoucherEntry.getIsSummary() == "1") {
                        if(ghsFDetailID == null || ghsFDetailID.isEmpty()) {
                            //核算项目
                            tVoucherEntry.setFDetailID("0");
                        } else {
                            //核算项目
                            tVoucherEntry.setFDetailID(String.valueOf(ghsFDetailID.get("FDetailID")));
                        }
                        //科目内码
                        tVoucherEntry.setFAccountID(ymlProp.getSubjectAccountsPayable());
                        //对方科目
                        tVoucherEntry.setFAccountID2(ymlProp.getSubjectStockGoods());
                    } else if(tVoucherEntry.getIsSummary() == "0") {
                        if(ghsFDetailID == null || ghsFDetailID.isEmpty()) {
                            //核算项目
                            tVoucherEntry.setFDetailID("0");
                        } else {
                            //核算项目
                            tVoucherEntry.setFDetailID(String.valueOf(ckFDetailId.get("FDetailID")));
                        }
                        //科目内码
                        tVoucherEntry.setFAccountID(ymlProp.getSubjectStockGoods());
                        //对方科目
                        tVoucherEntry.setFAccountID2(ymlProp.getSubjectAccountsPayable());
                    }
                    tVoucherEntry.setFVoucherID(FVoucherID);
                    incomingStatementKingDeeDao.addVoucherEntry(tVoucherEntry);
                }
            }
        }
    }
}
