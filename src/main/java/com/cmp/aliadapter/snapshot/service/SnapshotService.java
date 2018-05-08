package com.cmp.aliadapter.snapshot.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.image.model.req.ReqCreImage;
import com.cmp.aliadapter.snapshot.model.req.ReqCreSnapshot;
import com.cmp.aliadapter.snapshot.model.res.ResSnapshots;

public interface SnapshotService {

    /**
     * 查询快照列表
     *
     * @param cloud 云
     * @return 快照列表
     */
    ResSnapshots describeSnapshots(CloudEntity cloud);

    /**
     * 创建快照
     *
     * @param cloud          云
     * @param reqCreSnapshot 请求体
     */
    void createSnapshot(CloudEntity cloud, ReqCreSnapshot reqCreSnapshot);
}
