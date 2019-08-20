package org.spring.boot.multiple.ds.dao;

import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;
import org.springframework.stereotype.Component;

@Component
public interface GoodsIntoReturnKingDeeDao {

    /**
     * 插入凭证数据
     * @param tvoucher
     * @return
     */
    int insertPurchase(Tvoucher tvoucher);

    /**
     * 查询最后新增的数据
     * @return
     */
    Tvoucher selectLastVoucher();

    /**
     * 插入凭证分录数据
     * @param tvoucherEntry
     * @return
     */
    int insertVoucherEntry(TvoucherEntry tvoucherEntry);

    /**
     * 更新凭证数据
     * @param FVoucherID
     * @return
     */
    int updateVoucher(String FVoucherID);

    /**
     * 根据字段值查询编号
     * @return
     */
    Object selectFNumber(String name);
}
