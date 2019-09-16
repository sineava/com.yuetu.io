package org.spring.boot.multiple.ds.dao;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 刘世杰
 * @date 2019/8/19
 * 商品移仓单百盛dao
 */
@Component
public interface MerchandiseShiftBSDao {

    /**
     * 查询商品移仓单凭证数据(百盛)
     * @param date
     * @return
     */
    List<Object> getVoucher(DateInfo date);

    /**
     * 查询商品移仓单凭证分录数据(百盛)
     * @param date
     * @return
     */
    List<Object> getVoucherEntry(DateInfo date);

    List<Object> RemoveVoucher(DateInfo date);

    List<Object> RemoveVoucherEntry(DateInfo date);
}
