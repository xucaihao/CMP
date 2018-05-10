package com.cmp.aliadapter.image.service;

import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.image.model.req.ReqCreImage;
import com.cmp.aliadapter.image.model.res.ResImage;
import com.cmp.aliadapter.image.model.res.ResImages;

public interface ImageService {

    /**
     * 查询镜像列表
     *
     * @param cloud 云
     * @return 镜像列表
     */
    ResImages describeImages(CloudEntity cloud);

    /**
     * 查询指定镜像
     *
     * @param cloud    云（用户提供ak、sk）
     * @param regionId 区域id
     * @param imageId  镜像id
     * @return 指定镜像信息
     */
    ResImage describeImageAttribute(CloudEntity cloud, String regionId, String imageId);

    /**
     * 创建镜像
     *
     * @param cloud       云
     * @param reqCreImage 请求体
     */
    void createImage(CloudEntity cloud, ReqCreImage reqCreImage);

    /**
     * 删除镜像
     *
     * @param cloud    云（用户提供ak、sk）
     * @param regionId 区域id
     * @param imageId  镜像id
     */
    void deleteImage(CloudEntity cloud, String regionId, String imageId);

}
