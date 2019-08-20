package org.spring.boot.multiple.ds.util;

import org.spring.boot.multiple.ds.bean.Tvoucher;
import org.spring.boot.multiple.ds.bean.TvoucherEntry;

/**
 * 把凭证以及分录默认数据封装成一个实体进行返回
 */
public class VoucherUtil {

    /**
     * 返回包含凭证表默认数据的voucher对象
     * @return
     */
    public static Tvoucher getVoucher() {
        Tvoucher voucher = new Tvoucher();
        //摘要组内码
        voucher.setFGroupID("1");
        //参考信息
        voucher.setFReference(null);
        //摘要
        voucher.setFExplanation("");
        //附件张数
        voucher.setFAttachments("0");
        //空-手工凭证,非空-机制凭证
        voucher.setFInternalInd(null);
        //0-未审核,1-已审核
        voucher.setFChecked("0");
        //0-未过账,1-已过账
        voucher.setFPosted("0");
        //制单人
        voucher.setFPreparerID("16394");
        //审核人
        voucher.setFCheckerID("-1");
        //记账人
        voucher.setFPosterID("-1");
        //出纳员
        voucher.setFCashierID("-1");
        //会计主管
        voucher.setFHandler(null);
        //其他系统传入凭证对象接口描述
        voucher.setFObjectName(null);
        //接口参数
        voucher.setFParameter(null);
        //单据类型
        voucher.setFTranType("0");
        //制单人所属工作组
        voucher.setFOwnerGroupID("0");
        return voucher;
    }

    public static TvoucherEntry getVoucherEntry() {
        TvoucherEntry tvoucherEntry = new TvoucherEntry();
        //币别
        tvoucherEntry.setFCurrencyID("1");
        //费率类型
        tvoucherEntry.setFExchangeRateType("1");
        //费率
        tvoucherEntry.setFExchangeRate("1");
        //数量
        tvoucherEntry.setFQuantity("0");
        //单位内码
        tvoucherEntry.setFMeasureUnitID("0");
        //单价
        tvoucherEntry.setFUnitPrice("0");
        //机制凭证
        tvoucherEntry.setFInternalInd(null);
        //结算方式
        tvoucherEntry.setFSettleTypeID("0");
        //结算号
        tvoucherEntry.setFSettleNo(null);
        //现金流量 0-不是,1-是现金流量项目
        tvoucherEntry.setFCashFlowItem("0");
        //项目任务内码
        tvoucherEntry.setFTaskID("0");
        //项目资源ID
        tvoucherEntry.setFResourceID("0");
        //业务号
        tvoucherEntry.setFTransNo(null);
        return tvoucherEntry;
    }
}
