package org.spring.boot.multiple.ds;

import java.lang.annotation.*;

/**
 * @author donghongchen
 * @create 2017-09-04 14:41
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    String value();

}
