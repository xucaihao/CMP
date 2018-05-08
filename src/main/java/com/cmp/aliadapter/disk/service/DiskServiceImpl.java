package com.cmp.aliadapter.disk.service;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeDisksRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;
import com.aliyuncs.ecs.model.v20140526.ModifyDiskAttributeRequest;
import com.cmp.aliadapter.common.*;
import com.cmp.aliadapter.disk.model.req.ReqModifyDisk;
import com.cmp.aliadapter.disk.model.res.DiskInfo;
import com.cmp.aliadapter.disk.model.res.ResDisks;
import com.cmp.aliadapter.region.model.res.ResRegions;
import com.cmp.aliadapter.region.service.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DiskServiceImpl implements DiskService {

    private static final Logger logger = LoggerFactory.getLogger(DiskServiceImpl.class);

    @Autowired
    private RegionService regionService;

    private DiskInfo convertDisk(DescribeDisksResponse.Disk disk) {
        DiskInfo resDisk = new DiskInfo();
        resDisk.setDiskId(disk.getDiskId());
        resDisk.setDiskName(disk.getDiskName());
        resDisk.setRegionId(disk.getRegionId());
        resDisk.setDescription(disk.getDescription());
        resDisk.setType(disk.getType());
        resDisk.setCategory(disk.getCategory());
        resDisk.setEncrypted(disk.getEncrypted());
        resDisk.setSize(disk.getSize());
        resDisk.setStatus(disk.getStatus());
        resDisk.setInstanceId(disk.getInstanceId());
        if (!StringUtils.isEmpty(disk.getCreationTime())) {
            String createdTime = disk.getCreationTime()
                    .replace("T", " ")
                    .replace("Z", " ");
            resDisk.setCreationTime(createdTime);
        }
        resDisk.setDiskChargeType(disk.getDiskChargeType());
        resDisk.setPortable(disk.getPortable());
        return resDisk;
    }

    /**
     * 查询硬盘列表
     *
     * @param cloud 云
     * @return 硬盘列表
     */
    @Override
    public ResDisks describeDisks(CloudEntity cloud) {
        if (AliClient.getStatus()) {
            List<DescribeRegionsResponse.Region> regions = new ArrayList<>();
            DescribeRegionsResponse.Region regionInfo = new DescribeRegionsResponse.Region();
            regionInfo.setRegionId("cn-beijing");
            regions.add(regionInfo);
            ResRegions resRegions = new ResRegions(regions);
//            ResRegions resRegions = regionService.describeRegions(cloud);
            List<CompletionStage<List<DiskInfo>>> futures =
                    resRegions.getRegions().stream().map(region ->
                            CompletableFuture.supplyAsync(() -> {
                                //初始化
                                IAcsClient client = initClient(cloud, region.getRegionId());
                                //设置参数
                                DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
                                describeDisksRequest.setRegionId(region.getRegionId());
                                describeDisksRequest.setPageSize(DEFAULT_PAGE_SIZE);
                                AtomicInteger totalPage = new AtomicInteger(1);
                                List<DescribeDisksResponse.Disk> disks = new ArrayList<>();
                                for (AtomicInteger pageNum = new AtomicInteger(1);
                                     pageNum.get() <= totalPage.get();
                                     pageNum.incrementAndGet()) {
                                    try {
                                        CompletableFuture.runAsync(() -> {
                                            try {
                                                describeDisksRequest.setPageNumber(pageNum.get());
                                                DescribeDisksResponse response =
                                                        client.getAcsResponse(describeDisksRequest);
                                                disks.addAll(response.getDisks());
                                                totalPage.set((response.getTotalCount() / DEFAULT_PAGE_SIZE) + 1);
                                            } catch (Exception e) {
                                                ExceptionUtil.dealException(e, pageNum.get());
                                            }
                                        }).get(TIME_OUT_SECONDS, TimeUnit.SECONDS);
                                    } catch (Exception e) {
                                        break;
                                    }
                                }
                                return disks.stream()
                                        .map(this::convertDisk)
                                        .collect(toList());
                            })
                    ).collect(toList());
            List<DiskInfo> disks = CommonUtil.aggregateList(CommonUtil.joinRes(futures));
            return new ResDisks(disks);
        } else {
            List<DiskInfo> disks = AliSimulator.getAll(DiskInfo.class);
            return new ResDisks(disks);
        }
    }

    /**
     * 修改硬盘名称
     *
     * @param cloud         云（用户提供ak、sk）
     * @param reqModifyDisk 请求体
     */
    @Override
    public void modifyDiskName(CloudEntity cloud, ReqModifyDisk reqModifyDisk) {
        if (AliClient.getStatus()) {
            //初始化
            IAcsClient client = initClient(cloud, reqModifyDisk.getRegionId());
            //设置参数
            ModifyDiskAttributeRequest modifyDiskAttributeRequest = new ModifyDiskAttributeRequest();
            modifyDiskAttributeRequest.setRegionId(reqModifyDisk.getRegionId());
            modifyDiskAttributeRequest.setDiskId(reqModifyDisk.getDiskId());
            modifyDiskAttributeRequest.setDiskName(reqModifyDisk.getDiskName());
            // 发起请求
            try {
                client.getAcsResponse(modifyDiskAttributeRequest);
            } catch (Exception e) {
                logger.error("modifyDiskName error: {}", e.getMessage());
                ExceptionUtil.dealException(e);
            }
        }
    }
}
