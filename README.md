## 悦途数据抽取项目
![](https://img.shields.io/badge/JDK-1.8-green.svg)
![](https://img.shields.io/badge/release-1.0-blue.svg)
---
technology stack: <code>lombok</code>,<code>spring-boot</code>,<code>mybatis</code>,<code>druid</code>
#### 说明: 
> 结算项目更改请直接在resources/application.yml中直接进行配置,降低代码耦合度

```yml
- 重要表列举(待补充)
    t_voucher:凭证表
    t_voucherEntry:凭证分录表
    
    t_itemPropDesc:核算项目附表信息描述表
    t_itemDetailV:核算项目使用详情纵表
    t_itemDetail:核算项目使用详情横表
    t_item:核算项目表
    t_itemClass:核算项目类别表
    t_account:科目表

- 重要字段说明
t_itemClass表:
    FItemClassID(只列举几个重要的):
        ## 查询时请使用该类进行区别,否则可能导致查出多条数据
        0: *
        1: 客户
        2: 部门
        3: 职员
        5: 仓库
        8: 供应商
        
tVoucherEntry表:
    FDetail: 核算项目使用ID，0为不下设核算项目
        ## id来源:t_itemDetailV表下FDetailID字段
        -- 根据t_item:FName > t_item:FitemID = t_itemDetailV:FItemID > t_itemDetailV:FDeatilID
        此处使用的是t_itemDetailV:FDeatilID, fix:凭证抽取bug #1
```

- [x] 商品移仓单(移入):百盛(分销系统/移仓数据分析)-已自测
> 借: 移入仓(核算项目:库存商品);贷: 移出仓(核算项目:库存商品)

- [x] 商店配货单:百盛(分销系统/配货数据分析)-已自测
> 借: 商店(核算项目:库存商品);贷: 仓库(核算项目:库存商品)

- [x] 商店退货单:百盛(分销系统/配货数据分析)-已自测
> 借: 仓库(核算项目:库存商品);贷: 商店(核算项目:库存商品)

- [x] 商品进货单:百盛(分销系统/进货数据分析)-已自测
> 借: 仓库(核算项目:库存商品);贷: 供货商(核算项目:应收账款)

- [x] 商品退货单:百盛(分销系统/进货数据分析)-已自测
> 借: 供货商(核算项目:应收账款);贷: 仓库(核算项目:库存商品)

- [x] 进货结算单:百盛(财务系统/进货结算单)
> 借: 仓库(核算项目:库存商品);贷：供货商(核算项目:应收账款)

- [ ] 销售费用单:百盛(财务系统/销货费用数据分析)
> 代垫客户快递费(借:应收账款;贷:应付账款)
> 账扣促销费(借:营业费用;贷:应收账款)

#### 监控sql
- 凭证sql新增(金蝶K3监控)
```
    -- 凭证插入
    exec sp_executesql N'INSERT INTO t_VoucherEntry (FVoucherID,FEntryID,FExplanation,FAccountID,FCurrencyID,FExchangeRateType,FExchangeRate,FDC,FAmountFor,FAmount,FQuantity,FMeasureUnitID,FUnitPrice,FInternalInd,FAccountID2,FSettleTypeID,FSettleNo,FCashFlowItem,FTaskID,FResourceID,FTransNo,FDetailID) VALUES (@P1,@P2,@P3,@P4,@P5,@P6,@P7,@P8,@P9,@P10,@P11,@P12,@P13,@P14,@P15,@P16,@P17,@P18,@P19,@P20,@P21,@P22)',N'@P1 int,@P2 int,@P3 varchar(255),@P4 int,@P5 int,@P6 float,@P7 float,@P8 int,@P9 money,@P10 money,@P11 float,@P12 int,@P13 float,@P14 varchar(10),@P15 int,@P16 int,@P17 varchar(255),@P18 int,@P19 int,@P20 int,@P21 varchar(255),@P22 int',225,0,'测试数据001',1046,1,1,1,1,$1.0000,$1.0000,0,0,0,NULL,1039,0,NULL,0,0,0,NULL,128
    -- 凭证分录插入
    exec sp_executesql N'INSERT INTO t_Voucher (FDate,FTransDate,FYear,FPeriod,FGroupID,FNumber,FReference,FExplanation,FAttachments,FEntryCount,FDebitTotal,FCreditTotal,FInternalInd,FChecked,FPosted,FPreparerID,FCheckerID,FPosterID,FCashierID,FHandler,FObjectName,FParameter,FSerialNum,FTranType,FOwnerGroupID) VALUES (@P1,@P2,@P3,@P4,@P5,@P6,@P7,@P8,@P9,@P10,@P11,@P12,@P13,@P14,@P15,@P16,@P17,@P18,@P19,@P20,@P21,@P22,@P23,@P24,@P25)',N'@P1 datetime,@P2 datetime,@P3 int,@P4 int,@P5 int,@P6 int,@P7 varchar(255),@P8 varchar(255),@P9 int,@P10 int,@P11 money,@P12 money,@P13 varchar(10),@P14 bit,@P15 bit,@P16 int,@P17 int,@P18 int,@P19 int,@P20 varchar(50),@P21 varchar(100),@P22 varchar(100),@P23 int,@P24 int,@P25 int','2014-02-28 00:00:00','2014-02-28 00:00:00',2014,2,4,44,NULL,'测试数据001',0,2,$1.0000,$1.0000,NULL,0,0,16394,-1,-1,-1,NULL,NULL,NULL,183,0,1
```

#### 相关文件
- [ERP开发科目清理](http://t.cn/AiQSS7k0)
- [百盛数据字典](http://t.cn/AiQSoLTt)
- [K3凭证导入相关库表描述](http://t.cn/AiQS9bOC)
- [目前发现的缺失核算项目](http://t.cn/AiRdJ4TK)

---

#### 待处理事项
- 当不存在核算项目时(如供货商,仓库等),进行消息提示
- 单据日期目前使用的是单据抽取的实际日期,需要与客户协商
- 商店配货单在(2019-08-01 至 2019-08-31)发现一条分录数据为负,金额为-61.3

