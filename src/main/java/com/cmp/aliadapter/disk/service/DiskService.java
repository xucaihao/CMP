package com.cmp.aliadapter.disk.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.disk.model.req.ReqModifyDisk;
import com.cmp.aliadapter.disk.model.res.ResDisks;

public interface DiskService {

    /**
     * 查询硬盘列表
     *
     * @param cloud 云
     * @return 硬盘列表
     */
    ResDisks describeDisks(CloudEntity cloud);

    /**
     * 修改硬盘名称
     *
     * @param cloud         云（用户提供ak、sk）
     * @param reqModifyDisk 请求体
     */
    void modifyDiskName(CloudEntity cloud, ReqModifyDisk reqModifyDisk);
}
