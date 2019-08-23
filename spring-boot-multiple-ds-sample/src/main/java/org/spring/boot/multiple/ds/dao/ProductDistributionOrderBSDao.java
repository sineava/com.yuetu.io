package org.spring.boot.multiple.ds.dao;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 刘世杰
 * @date 2019/8/22
 * 商店配货退货单百盛dao
 */
@Component
public interface ProductDistributionOrderBSDao {

    /**
     * 查询商店配货单凭证数据(百盛)
     * @param date
     * @return
     */
    List<Object> getVoucher(DateInfo date);

    /**
     * 查询商店配货单凭证分录数据(百盛)
     * @param date
     * @return
     */
    List<Object> getVoucherEntry(DateInfo date);

    /**
     * 查询商店退货单凭证数据(百盛)
     * @param date
     * @return
     */
    List<Object> getReturnVoucher(DateInfo date);

    /**
     * 查询商店退货单凭证分录数据(百盛)
     * @param date
     * @return
     */
    List<Object> getReturnVoucherEntry(DateInfo date);
}
