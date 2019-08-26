package org.spring.boot.multiple.ds.service.impl;

import org.spring.boot.multiple.ds.TargetDataSource;
import org.spring.boot.multiple.ds.dao.IsExtractDao;
import org.spring.boot.multiple.ds.service.IsExtractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 刘世杰
 * @date 2019/8/26
 * 判断数据是否进行了抽取,避免重复抽取数据
 */
@SuppressWarnings("unchecked")
@Service
public class IsExtractServiceImpl implements IsExtractService {

    @Autowired
    IsExtractDao isExtractDao;

    @TargetDataSource("ds1")
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Object isExtract(String type) {
        return isExtractDao.isExtract(type);
    }

    @TargetDataSource("ds1")
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void changeExtractStatus(String type) {
        isExtractDao.changeExtractStatus(type);
    }
}
