## 悦途数据抽取项目
![](https://img.shields.io/badge/JDK-1.8-green.svg)
![](https://img.shields.io/badge/release-1.0-blue.svg)
---
technology stack: <code>lombok</code>,<code>spring-boot</code>,<code>mybatis</code>
#### 说明: 
> 结算项目更改请直接在resources/application.yml中直接进行配置,降低代码耦合度

```
- 重要字段说明
t_itemClass表:
    FItemClassID(只列举几个重要的,其他的查看请查看表):
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

- [x] 商品移仓单
> 借: 移入仓(核算项目:库存商品);贷: 移出仓(核算项目:库存商品)

- [x] 商店配货单
> 借: 商店(核算项目:库存商品);贷: 仓库(核算项目:库存商品)

- [x] 商店退货单
> 借: 仓库(核算项目:库存商品);贷: 商店(核算项目:库存商品)

- [x] 商品进货单
> 借: 仓库(核算项目:库存商品);贷: 供货商(核算项目:应收账款)

- [x] 商品退货单
> 借: 供货商(核算项目:应收账款);贷: 仓库(核算项目:库存商品)

#### 相关文件
- [ERP开发科目清理](http://t.cn/AiQSS7k0)
- [百盛数据字典](http://t.cn/AiQSoLTt)
- [K3凭证导入相关库表描述](http://t.cn/AiQS9bOC)

