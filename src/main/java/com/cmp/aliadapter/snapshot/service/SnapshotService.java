package com.cmp.aliadapter.snapshot.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.image.model.res.ResImages;
import com.cmp.aliadapter.snapshot.model.ResSnapshots;

public interface SnapshotService {

    /**
     * 查询快照列表
     *
     * @param cloud 云
     * @return 快照列表
     */
    ResSnapshots describeSnapshots(CloudEntity cloud);
}
