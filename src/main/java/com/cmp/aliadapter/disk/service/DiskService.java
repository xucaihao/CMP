package com.cmp.aliadapter.disk.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.eip.modle.ResEipAddresses;

public interface DiskService {

    /**
     * 查询实例列表
     *
     * @param cloud    云
     * @param regionId 地域
     * @return 实例列表
     */
    ResEipAddresses describeDisks(CloudEntity cloud, String regionId);
}
