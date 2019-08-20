package org.spring.boot.multiple.ds.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 凭证分录表
 * @author 刘世杰
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TvoucherEntry {

    private String FVoucherID;
    private String FEntryID;
    private String FExplanation;
    private String FAccountID;
    private String FCurrencyID;
    private String FExchangeRateType;
    private String FExchangeRate;
    private String FDC;
    private String FAmountFor;
    private String FAmount;
    private String FQuantity;
    private String FMeasureUnitID;
    private String FUnitPrice;
    private String FInternalInd;
    private String FAccountID2;
    private String FSettleTypeID;
    private String FSettleNo;
    private String FCashFlowItem;
    private String FTaskID;
    private String FResourceID;
    private String FTransNo;
    private String FDetailID;

    /**
     * add 2019/8/15
     * CKMC: 仓库名称
     * GHSMC: 供货商名称
     */
    private String CKMC;
    private String GHSMC;

    /**
     * add 2019/8/20
     * debit: 借方
     * credit: 贷方
     */
    private String debit;
    private String credit;
}
