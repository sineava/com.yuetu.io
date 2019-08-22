package org.spring.boot.multiple.ds.service;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;

import java.util.List;

/**
 * @author 刘世杰
 * @date 2019/8/22
 * ProductDistributionOrdeService:商店配货退货单
 */
public interface ProductDistributionOrderService {

    /**
     * 商店配货单-查询凭证所需数据(百盛)
     * @param date:抽取日期
     * @return
     */
    List<Tvoucher> productDistributionOrderVoucher(DateInfo date) throws Exception;

    /**
     * 商品移仓单-查询凭证分录所需数据(百盛)
     * @param date:抽取日期
     * @return
     */
    List<TvoucherEntry> productDistributionOrderVoucherEntry(DateInfo date) throws Exception;

    /**
     * 商品移仓单-插入凭证数据(金蝶)
     * @param tVoucherList
     * @param tVoucherEntryList
     * @return
     */
    void insertProductDistributionOrderVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList) throws Exception;

}
