package org.spring.boot.multiple.ds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author donghongchen
 * @create 2017-09-06 11:28
 **/
@SpringBootApplication
@Import(DynamicDataSourceRegister.class)
@MapperScan("org.spring.boot.multiple.ds.dao")
@ServletComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

}
