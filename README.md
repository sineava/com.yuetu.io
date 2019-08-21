## 悦途数据抽取项目
![](https://img.shields.io/badge/JDK-1.8-green.svg)
![](https://img.shields.io/badge/release-1.0-blue.svg)
---
technology stack: <code>lombok</code>,<code>spring-boot</code>,<code>mybatis</code>
#### 说明: 
> 结算项目更改请直接在resources/application.yml中直接进行配置,降低代码耦合度

- 商品移仓单
> 借: 移入仓(核算项目:库存商品);贷: 移出仓(核算项目:库存商品)

- 商品进货单
> 借: 仓库(核算项目:库存商品);贷: 供货商(核算项目:应收账款)

- 商品退货单
> 借: 供货商(核算项目:应收账款);贷: 仓库(核算项目:库存商品)

#### 相关文件
- [ERP开发科目清理](http://t.cn/AiQSS7k0)
- [百盛数据字典](http://t.cn/AiQSoLTt)

