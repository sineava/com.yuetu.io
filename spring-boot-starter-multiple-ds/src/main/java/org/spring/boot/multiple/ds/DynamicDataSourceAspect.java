package org.spring.boot.multiple.ds;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author donghongchen
 * @create 2017-09-04 14:44
 * <p>
 * 切换数据源Advice
 **/
@Aspect
@Order(-10) //保证该aop在@Transaction之前执行
@Component
public class DynamicDataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * * @Before("@annotation(ds)")
     * 的意思是：@Before：在方法执行之前进行执行; @annotation(targetDataSource)：会拦截注解targetDataSource的方法，否则不拦截;
     * @param point
     * @param targetDataSource
     */
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource){
        //获取当前的指定数据源
        String dsID = targetDataSource.value();
        //如果不在我们注入的所有的数据源范围内，输出警告信息，系统自动使用默认的数据源
        if (!DynamicDataSourceContextHolder.containsDataSource(dsID)){
            logger.error("数据源["+dsID+"]不存在，使用默认的数据源 > { " + dsID+", 方法签名："+point.getSignature()+"}");
        }else {
            logger.info("Use DataSource:   {" +dsID+", 方法签名："+point.getSignature() +"}");
            //找到的话，那么设置动态数据源上下文
            DynamicDataSourceContextHolder.setDataSourceType(dsID);
        }
    }


    @After("@annotation(targetDataSource)")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource){
        //方法执行完毕后，销毁当前数据源信息，进行垃圾回收
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
}

