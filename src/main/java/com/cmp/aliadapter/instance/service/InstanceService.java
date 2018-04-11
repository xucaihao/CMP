package com.cmp.aliadapter.instance.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.instance.model.res.ResInstance;
import com.cmp.aliadapter.instance.model.res.ResInstances;

public interface InstanceService {

    /**
     * 查询实例列表
     *
     * @param cloud 云
     * @return 实例列表
     */
    ResInstances describeInstances(CloudEntity cloud);

    /**
     * 查询指定实例
     *
     * @param cloud      云
     * @param regionId   地域
     * @param instanceId 实例id
     * @return 指定实例
     */
    ResInstance describeInstance(CloudEntity cloud, String regionId, String instanceId);
}
