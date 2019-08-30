package org.spring.boot.multiple.ds.dao;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 刘世杰
 * @date 2019/8/29
 * 进货结算单百盛dao
 */
@Component
public interface IncomingStatementBSDao {

    /**
     * 查询进货结算单凭证数据(百盛)
     * @param date
     * @return
     */
    List<Object> getVoucher(DateInfo date);

    /**
     * 查询进货结算单分录凭证数据(百盛)
     * @param DJBH:单据编号
     * @return
     */
    List<Object> getVoucherEntry(String DJBH);

    /**
     * 根据供货商代码查询供货商名称-table:GONGHUOSHANG
     * @param dm1:供货商代码
     * @return
     */
    Object getGHSMC(String dm1);

    /**
     * 根据供货商代码查询供货商名称-table:GONGHUOSHANG
     * @param dm2:仓库代码
     * @return
     */
    Object getCKMC(String dm2);
}
