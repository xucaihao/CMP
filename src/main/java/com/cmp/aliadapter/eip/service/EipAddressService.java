package com.cmp.aliadapter.eip.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.eip.modle.ResEipAddress;
import com.cmp.aliadapter.eip.modle.ResEipAddresses;
import com.cmp.aliadapter.eip.modle.req.ReqAllocateEipAddress;

public interface EipAddressService {

    /**
     * 查询弹性公网ip列表
     *
     * @param cloud    云
     * @param regionId 地域
     * @return 弹性公网ip列表
     */
    ResEipAddresses describeEipAddresses(CloudEntity cloud, String regionId);

    /**
     * 查询指定弹性公网ip
     *
     * @param cloud        云
     * @param regionId     地域
     * @param allocationId 弹性公网ip的id
     * @return 指定弹性公网ip
     */
    ResEipAddress describeEipAddressAttribute(CloudEntity cloud, String regionId, String allocationId);

    /**
     * 创建弹性公网ip
     *
     * @param cloud                 云
     * @param reqAllocateEipAddress 请求体
     * @return 操作结果
     */
    ResEipAddress AllocateEipAddress(CloudEntity cloud, ReqAllocateEipAddress reqAllocateEipAddress);

}
