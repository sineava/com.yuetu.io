package org.spring.boot.multiple.ds.dao;

import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.springframework.stereotype.Component;

/**
 * @author 刘世杰
 * @date 2019/9/20
 * 销货收款单金蝶dao
 */
@Component
public interface SalesReceiptKingDeeDao {

    /**
     * 查询新增前的最后一条凭证数据
     * resolve: 无法获取自增主键
     * @return
     */
    Tvoucher selectLastVoucher();

    /**
     * 新增凭证数据
     * @param tvoucher: 凭证数据
     * @return
     */
    void addVoucher(Tvoucher tvoucher);
}
