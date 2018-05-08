package com.cmp.aliadapter.image.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.image.model.req.ReqCreImage;
import com.cmp.aliadapter.image.model.res.ResImages;
import com.cmp.aliadapter.instance.model.req.ReqModifyInstance;

public interface ImageService {

    /**
     * 查询镜像列表
     *
     * @param cloud 云
     * @return 镜像列表
     */
    ResImages describeImages(CloudEntity cloud);

    /**
     * 创建镜像
     *
     * @param cloud       云
     * @param reqCreImage 请求体
     */
    void createImage(CloudEntity cloud, ReqCreImage reqCreImage);

}
