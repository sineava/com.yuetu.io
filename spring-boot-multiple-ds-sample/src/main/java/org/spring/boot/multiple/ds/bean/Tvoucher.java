package org.spring.boot.multiple.ds.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 凭证表
 * @author 刘世杰
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tvoucher {
    private String FDate;
    private String FTransDate;
    private String FVoucherID;
    private String FYear;
    private String FPeriod;
    private String FGroupID;
    private String FNumber;
    private String FReference;
    private String FExplanation;
    private String FAttachments;
    private String FEntryCount;
    private String FDebitTotal;
    private String FCreditTotal;
    private String FInternalInd;
    private String FChecked;
    private String FPosted;
    private String FPreparerID;
    private String FCheckerID;
    private String FPosterID;
    private String FCashierID;
    private String FHandler;
    private String FObjectName;
    private String FParameter;
    private String FSerialNum;
    private String FTranType;
    private String FOwnerGroupID;

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

    /**
     * add 2019/8/29
     * summary: 汇总核算科目
     */
    private String summary;

    /**
     * add 2019/8/30
     * djbh: 通过单据编号将凭证List以及凭证分录List进行汇总
     */
    private String djbh;

    /**
     * add 2019/9/20
     * khmc: 客户名称
     */
    private String khmc;

    /**
     * add 2019/9/20
     * skfs: 收款方式
     */
    private String skfs;
}
