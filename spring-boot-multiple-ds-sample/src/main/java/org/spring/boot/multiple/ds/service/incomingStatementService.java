package org.spring.boot.multiple.ds.service;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;

import java.util.List;
import java.util.Map;

/**
 * @author 刘世杰
 * @date 2019/8/29
 * incomingStatementService:进货结算单
 */
public interface incomingStatementService {

    /**
     * 进货结算单-查询凭证所需数据(百盛)
     * @param date:抽取日期
     * @return
     */
    Map<String, List<Object>> incomingStatementVoucher(DateInfo date) throws Exception;

    /**
     * 进货结算单-插入凭证数据(金蝶)
     * @param tVoucherList
     * @param tVoucherEntryList
     * @return
     */
    void insertIncomingStatementVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList) throws Exception;

}
