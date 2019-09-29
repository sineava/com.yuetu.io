package org.spring.boot.multiple.ds.dao;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 刘世杰
 * @date 2019/9/20
 * 销货收款单百盛dao
 */
@Component
public interface SalesReceiptBSDao {

    /**
     * 查询销货收款单凭证数据(百盛)
     * @param date
     * @return
     */
    List<Object> getVoucher(DateInfo date);

    /**
     * 查询销货收款单凭证分录数据(百盛)
     * @param djbh:单据编号
     * @return
     */
    List<Object> getVoucherEntry(String djbh);

    /**
     * 根据客户代码查询客户名称-table:KEHU
     * @param dm1:客户代码
     * @return
     */
    Object getKHMC(String dm1);

    /**
     * 根据客户代码查询客户名称-table:KEHU
     * @param skzh:收款账号
     * @return
     */
    Object getZHMC(String skzh);
}
