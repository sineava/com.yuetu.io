package org.spring.boot.multiple.ds;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author donghongchen
 * @create 2017-09-04 15:34
 * <p>
 * 动态数据源注册
 **/
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //如果配置文件中未指定数据源类型，使用默认值
    private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";

    private ConversionService conversionService = new DefaultConversionService();

    private PropertyValues dataSourcePropertyValues;
    //默认数据源
    private DataSource defaultDataSource;

    private Map<String, DataSource> customDataSources = new HashMap<>();

    /**
     * 加载多数据源配置
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        initDefaultDataSource(environment);
        initCustomDataSources(environment);
    }

    private void initDefaultDataSource(Environment environment) {
        //读取主数据源
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
        Map<String, Object> dsMap = new HashMap<>();
        dsMap.put("type", propertyResolver.getProperty("type"));
        dsMap.put("driverClassName", propertyResolver.getProperty("driverClassName"));
        dsMap.put("url", propertyResolver.getProperty("url"));
        dsMap.put("username", propertyResolver.getProperty("username"));
        dsMap.put("password", propertyResolver.getProperty("password"));
        //创建数据源
        defaultDataSource = buildDataSource(dsMap);
        dataBinder(defaultDataSource, environment);
    }


    private void initCustomDataSources(Environment environment) {
        //读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "custom.datasource.");
        String dsPrefixs = propertyResolver.getProperty("names");
        if (null == dsPrefixs || "".equals(dsPrefixs)) {
            return;
        }
        String[] dsPrefixsArr = dsPrefixs.split(",");
        for (String dsPrefix : dsPrefixsArr) {
            Map<String, Object> dsMap = propertyResolver.getSubProperties(dsPrefix + ".");
            DataSource ds = buildDataSource(dsMap);
            customDataSources.put(dsPrefix, ds);
            dataBinder(ds, environment);
        }
    }

    private DataSource buildDataSource(Map<String, Object> dsMap) {
        Object type = dsMap.get("type");
        if (type == null) {
            type = DATASOURCE_TYPE_DEFAULT;
        }
        Class<? extends DataSource> dataSourceType;
        try {
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);
            String driverClassName = dsMap.get("driverClassName").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();

            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setDriverClassName(driverClassName);

            return dataSource;

//            DataSourceBuilder factory = DataSourceBuilder.create().
//                    driverClassName(driverClassName).type(dataSourceType).url(url).username(username).password(password);
//            return factory.build();

        } catch (ClassNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    private void dataBinder(DataSource dataSource, Environment environment) {
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
        dataBinder.setConversionService(conversionService);
        dataBinder.setIgnoreNestedProperties(false);
        dataBinder.setIgnoreInvalidFields(false);
        dataBinder.setIgnoreUnknownFields(true);
        if (dataSourcePropertyValues == null) {
            Map<String, Object> rpr = new RelaxedPropertyResolver(environment, "spring.datasource").
                    getSubProperties(".");
            Map<String, Object> values = new HashMap<>(rpr);
            //排除已经设置的属性
            values.remove("type");
            values.remove("driverClassName");
            values.remove("url");
            values.remove("username");
            values.remove("password");
            dataSourcePropertyValues = new MutablePropertyValues(values);
        }
        dataBinder.bind(dataSourcePropertyValues);
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> targetDataSource = new HashMap<>();
        //将主数据源添加到更多数据源中
        targetDataSource.put("dataSource", defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIDS.add("dataSource");

        //添加更多数据源
        targetDataSource.putAll(customDataSources);
        for (String key : customDataSources.keySet()) {
            DynamicDataSourceContextHolder.dataSourceIDS.add(key);
        }
        //创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        //添加属性
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSource);
        registry.registerBeanDefinition("dataSource", beanDefinition);


    }


}
