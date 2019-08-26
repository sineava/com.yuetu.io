package org.spring.boot.multiple.ds.dao;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 判断是否进行了单据抽取,避免重复抽取
 * @author 刘世杰
 * @date 2019/8/26
 */
@Component
public interface IsExtractDao {

    /**
     * 判断该类别是否存在表中
     * @param type
     * @return
     */
    Map<String,Object> isExtract(String type);

    /**
     * 改变单据抽取状态
     */
    void changeExtractStatus(String type);
}
