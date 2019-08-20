package org.spring.boot.multiple.ds.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 刘世杰
 * @date 2019/8/19
 * <code>yml属性值-属性在yml文件进行修改</ode>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class YmlProp {

    /**
     * 应付账款
     */
    @Value("${prop.subject.accountsPayable}")
    private String subjectAccountsPayable;

    /**
     * 库存商品
     */
    @Value("${prop.subject.stockGoods}")
    private String subjectStockGoods;
}
