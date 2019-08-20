package org.spring.boot.multiple.ds.servlet.druid;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * druid过滤器.
 *
 * @author donghongchen
 * @create 2017/3/31.
 */
@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*",
        initParams = {
                @WebInitParam(name = "exclusions", value = ".js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*") // 忽略资源
        })
public class DruidStatFilter extends WebStatFilter {
}
