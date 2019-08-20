package org.spring.boot.multiple.ds.service;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;

import java.util.List;

/**
 * @author 刘世杰
 * @date 2019/8/19
 * MerchandiseShiftService:商品移仓单
 */
public interface MerchandiseShiftService {

    /**
     * 商品移仓单-查询凭证所需数据(百盛)
     * @param date:抽取日期
     * @return
     */
    List<Tvoucher> merchandiseShiftVoucher(DateInfo date) throws Exception;

    /**
     * 商品移仓单-查询凭证分录所需数据(百盛)
     * @param date:抽取日期
     * @return
     */
    List<TvoucherEntry> merchandiseShiftVoucherEntry(DateInfo date) throws Exception;

    /**
     * 商品移仓单-插入凭证数据(金蝶)
     * @param tVoucherList
     * @param tVoucherEntryList
     * @return
     */
    void insertMerchandiseShiftVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList) throws Exception;
}
