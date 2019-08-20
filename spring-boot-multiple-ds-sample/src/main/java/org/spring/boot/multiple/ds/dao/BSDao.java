package org.spring.boot.multiple.ds.dao;

import org.spring.boot.multiple.ds.bean.DateInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BSDao {
    List<Object> generatePurchase(DateInfo date);

    List<Object> selectEntry(DateInfo date);

    List<Object> returnGoodsVoucher(DateInfo date);

    List<Object> returnGoodsVoucherEntry(DateInfo date);
}
