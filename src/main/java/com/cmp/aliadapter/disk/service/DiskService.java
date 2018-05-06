package com.cmp.aliadapter.disk.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.disk.model.res.ResDisks;

public interface DiskService {

    /**
     * 查询硬盘列表
     *
     * @param cloud    云
     * @return 硬盘列表
     */
    ResDisks describeDisks(CloudEntity cloud);
}
