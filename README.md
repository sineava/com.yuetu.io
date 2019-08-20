# spring-boot-starter-multiple-ds
基于springboot开箱即用的多数据源动态调整插件，在service层使用注解@TargetDataSource("ds1")可以直接切换数据源，如果不使用，调用默认配置的数据源


# Quick Start

介绍
---
此插件由以下组成
- multiple.ds：读写数据源的代理，支持多写多读，用户只需在service层使用注解@TargetDataSource("ds1")，即可实现数据源动态切换。

spring-boot 配置
---
```
spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/foo?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=123456

# 更多数据源
custom.datasource.names=ds1,ds2
custom.datasource.ds1.driverClassName =com.mysql.jdbc.Driver
custom.datasource.ds1.url=jdbc:mysql://localhost:3306/test1
custom.datasource.ds1.username=root
custom.datasource.ds1.password=123456

custom.datasource.ds2.driverClassName =com.mysql.jdbc.Driver
custom.datasource.ds2.url=jdbc:mysql://localhost:3306/test2
custom.datasource.ds2.username=root
custom.datasource.ds2.password=123456


```

XML配置方式暂无
---


总结
---
用户只需在service层使用注解@TargetDataSource("ds1")，其他配置不变，即可实现数据源动态切换，对代码入侵低，配置简单，非常方便老项目的迁移。

