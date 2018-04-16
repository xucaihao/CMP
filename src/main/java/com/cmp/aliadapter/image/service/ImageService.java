package com.cmp.aliadapter.image.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.image.model.res.ResImages;

public interface ImageService {

    /**
     * 查询镜像列表
     *
     * @param cloud 云
     * @return 镜像列表
     */
    ResImages describeImages(CloudEntity cloud);
}
