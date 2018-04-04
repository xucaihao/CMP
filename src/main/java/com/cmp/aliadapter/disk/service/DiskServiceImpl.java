package com.cmp.aliadapter.disk.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.eip.modle.ResEipAddresses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DiskServiceImpl implements DiskService {

    private static final Logger logger = LoggerFactory.getLogger(DiskServiceImpl.class);

    /**
     * 查询实例列表
     *
     * @param cloud    云
     * @param regionId 地域
     * @return 实例列表
     */
    @Override
    public ResEipAddresses describeDisks(CloudEntity cloud, String regionId) {
        return null;
    }
}
