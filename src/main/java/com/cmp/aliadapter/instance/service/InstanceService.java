package com.cmp.aliadapter.instance.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.instance.model.req.ReqCloseInstance;
import com.cmp.aliadapter.instance.model.req.ReqModifyInstance;
import com.cmp.aliadapter.instance.model.req.ReqStartInstance;
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

    /**
     * 关闭实例
     *
     * @param cloud            云（用户提供ak、sk）
     * @param reqCloseInstance 请求体
     */
    void closeInstance(CloudEntity cloud, ReqCloseInstance reqCloseInstance);

    /**
     * 启动实例
     *
     * @param cloud            云（用户提供ak、sk）
     * @param reqStartInstance 请求体
     */
    void startInstance(CloudEntity cloud, ReqStartInstance reqStartInstance);

    /**
     * 修改实例名称
     *
     * @param cloud             云（用户提供ak、sk）
     * @param reqModifyInstance 请求体
     */
    void modifyInstanceName(CloudEntity cloud, ReqModifyInstance reqModifyInstance);

    /**
     * 重置实例密码
     *
     * @param cloud             云（用户提供ak、sk）
     * @param reqModifyInstance 请求体
     */
    void resetInstancesPassword(CloudEntity cloud, ReqModifyInstance reqModifyInstance);
}
