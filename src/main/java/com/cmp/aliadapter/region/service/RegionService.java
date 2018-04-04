package com.cmp.aliadapter.region.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.region.model.res.ResRegions;

public interface RegionService {

    /**
     * 查询所有地域列表
     *
     * @param cloud 云
     * @return 所有地域列表
     */
    ResRegions describeRegions(CloudEntity cloud);
}
