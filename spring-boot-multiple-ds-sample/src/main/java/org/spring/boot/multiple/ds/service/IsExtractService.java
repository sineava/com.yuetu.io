package org.spring.boot.multiple.ds.service;

/**
 * @author 刘世杰
 * @date 2019/8/26
 * 判断数据是否进行了抽取,避免重复抽取数据
 */
public interface IsExtractService {

    /**
     * 判断数据是否进行了抽取
     * @param type
     * @return
     */
    Object isExtract(String type);

    /**
     * 改变数据抽取状态,抽取完成将状态改为1
     * @param type
     */
    void changeExtractStatus(String type);
}
