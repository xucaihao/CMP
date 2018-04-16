package com.cmp.aliadapter.image.service;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.cmp.aliadapter.common.*;
import com.cmp.aliadapter.image.model.res.ImageInfo;
import com.cmp.aliadapter.image.model.res.ResImages;
import com.cmp.aliadapter.instance.model.res.ResInstances;
import com.cmp.aliadapter.region.model.res.ResRegions;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.cmp.aliadapter.common.AliClient.initClient;
import static com.cmp.aliadapter.common.Constance.DEFAULT_PAGE_SIZE;
import static com.cmp.aliadapter.common.Constance.TIME_OUT_SECONDS;
import static java.util.stream.Collectors.toList;

@Service
public class ImageServiceImpl implements ImageService {

    private ImageInfo convertImage(DescribeImagesResponse.Image image, String regionId) {
        ImageInfo resImage = new ImageInfo();
        resImage.setImageId(image.getImageId());
        resImage.setImageName(image.getImageName());
        resImage.setRegionId(regionId);
        if (!StringUtils.isEmpty(image.getCreationTime())) {
            String createdTime = image.getCreationTime()
                    .replace("T", " ")
                    .replace("Z", " ");
            resImage.setCreationTime(createdTime);
        }
        resImage.setPlatform(image.getPlatform());
        resImage.setArchitecture(image.getArchitecture());
        resImage.setSize(image.getSize());
        resImage.setOsName(image.getOSName());
        resImage.setSupportCloudInit(image.getIsSupportCloudinit());
        resImage.setImageOwnerAlias(image.getImageOwnerAlias());
        resImage.setDescription(image.getDescription());
        resImage.setStatus(image.getStatus());
        return resImage;
    }

    /**
     * 查询镜像列表
     *
     * @param cloud 云
     * @return 镜像列表
     */
    @Override
    public ResImages describeImages(CloudEntity cloud) {
        if (AliClient.getStatus()) {
            List<DescribeRegionsResponse.Region> regions = new ArrayList<>();
            DescribeRegionsResponse.Region regionInfo = new DescribeRegionsResponse.Region();
            regionInfo.setRegionId("cn-beijing");
            regions.add(regionInfo);
            ResRegions resRegions = new ResRegions(regions);
//            ResRegions resRegions = regionService.describeRegions(cloud);
            List<CompletionStage<List<ImageInfo>>> futures =
                    resRegions.getRegions().stream().map(region ->
                            CompletableFuture.supplyAsync(() -> {
                                //初始化
                                IAcsClient client = initClient(cloud, region.getRegionId());
                                //设置参数
                                DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest();
                                describeImagesRequest.setRegionId(region.getRegionId());
                                describeImagesRequest.setPageSize(DEFAULT_PAGE_SIZE);
                                AtomicInteger totalPage = new AtomicInteger(1);
                                List<DescribeImagesResponse.Image> images = new ArrayList<>();
                                for (AtomicInteger pageNum = new AtomicInteger(1);
                                     pageNum.get() <= totalPage.get();
                                     pageNum.incrementAndGet()) {
                                    try {
                                        CompletableFuture.runAsync(() -> {
                                            try {
                                                describeImagesRequest.setPageNumber(pageNum.get());
                                                DescribeImagesResponse response =
                                                        client.getAcsResponse(describeImagesRequest);
                                                images.addAll(response.getImages());
                                                totalPage.set((response.getTotalCount() / DEFAULT_PAGE_SIZE) + 1);
                                            } catch (Exception e) {
                                                ExceptionUtil.dealException(e, pageNum.get());
                                            }
                                        }).get(TIME_OUT_SECONDS, TimeUnit.SECONDS);
                                    } catch (Exception e) {
                                        break;
                                    }
                                }
                                return images.stream()
                                        .map(image -> convertImage(image, region.getRegionId()))
                                        .collect(toList());
                            })
                    ).collect(toList());
            List<ImageInfo> images = CommonUtil.aggregateList(CommonUtil.joinRes(futures));
            return new ResImages(images);
        } else {
            List<ImageInfo> images = AliSimulator.getAll(ImageInfo.class);
            return new ResImages(images);
        }
    }
}
