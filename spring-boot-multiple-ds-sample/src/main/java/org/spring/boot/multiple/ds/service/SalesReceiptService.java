package org.spring.boot.multiple.ds.service;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;

import java.util.List;
import java.util.Map;

/**
 * @author 刘世杰
 * @date 2019/9/20
 * @see SalesReceiptService:销货收款单
 */
public interface SalesReceiptService {

    /**
     * 销货收款单-查询凭证与分录所需数据(百盛)
     * @param date:抽取日期
     * @return map for voucher and voucherEntry from BS system
     * @exception
     */
    Map<String, List<Object>> salesReceiptVoucher(DateInfo date) throws Exception;

    /**
     * 销货收款单-插入凭证数据(金蝶)
     * @param tVoucherList
     * @param tVoucherEntryList
     * @return
     */
    void insertSalesReceiptVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList);
}
