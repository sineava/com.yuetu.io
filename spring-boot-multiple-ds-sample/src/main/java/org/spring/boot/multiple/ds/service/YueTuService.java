package org.spring.boot.multiple.ds.service;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;

import java.util.List;

/**
 * @author 刘世杰
 * @date 2019/8/14
 * goodsReceipt:商品进货单
 * goodsReturnReceipt:商品退货单
 */
public interface YueTuService {
    /**
     * 商品进货单-查询凭证所需数据(百盛)
     * @param date
     * @return
     */
    List<Tvoucher> goodsReceiptVoucher(DateInfo date) throws Exception;

    /**
     * 商品进货单-查询凭证分录所需数据(百盛)
     * @param date
     * @return
     */
    List<TvoucherEntry> goodsReceiptVoucherEntry(DateInfo date) throws Exception;

    /**
     * 商品进货单-插入凭证数据(金蝶)
     * @param tVoucherList
     * @param tVoucherEntryList
     * @return
     */
    int insertGoodsReceiptVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList) throws Exception;

    /**
     * 商品退货单-查询凭证所需数据(百盛)
     * @param date
     * @return
     */
    List<Tvoucher> goodsReturnReceiptVoucher(DateInfo date) throws Exception;

    /**
     * 商品退货单-查询凭证分录所需数据(百盛)
     * @param date
     * @return
     */
    List<TvoucherEntry> goodsReturnVoucherEntry(DateInfo date) throws Exception;

    /**
     * 商品退货单-插入凭证数据(金蝶)
     * @param tVoucherList
     * @param tVoucherEntryList
     * @return
     */
    int insertGoodsReturnReceiptVoucher(List<Tvoucher> tVoucherList, List<TvoucherEntry> tVoucherEntryList) throws Exception;
}
