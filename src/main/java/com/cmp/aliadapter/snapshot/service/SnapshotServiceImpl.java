package com.cmp.aliadapter.snapshot.service;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.CreateSnapshotRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeSnapshotsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeSnapshotsResponse;
import com.cmp.aliadapter.common.*;
import com.cmp.aliadapter.region.model.res.ResRegions;
import com.cmp.aliadapter.region.service.RegionService;
import com.cmp.aliadapter.snapshot.model.req.ReqCreSnapshot;
import com.cmp.aliadapter.snapshot.model.res.ResSnapshots;
import com.cmp.aliadapter.snapshot.model.res.SnapshotInfo;
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
public class SnapshotServiceImpl implements SnapshotService {

    private static final Logger logger = LoggerFactory.getLogger(SnapshotServiceImpl.class);

    @Autowired
    private RegionService regionService;

    private SnapshotInfo convertSnapshot(DescribeSnapshotsResponse.Snapshot snapshot, String regionId) {
        SnapshotInfo resSnapshot = new SnapshotInfo();
        resSnapshot.setSnapshotId(snapshot.getSnapshotId());
        resSnapshot.setSnapshotName(snapshot.getSnapshotName());
        resSnapshot.setStatus(snapshot.getStatus());
        resSnapshot.setPercent(Integer.valueOf(snapshot.getProgress().replace("%", "")));
        if (!StringUtils.isEmpty(snapshot.getCreationTime())) {
            String createdTime = snapshot.getCreationTime()
                    .replace("T", " ")
                    .replace("Z", " ");
            resSnapshot.setCreationTime(createdTime);
        }
        resSnapshot.setEncrypted(snapshot.getEncrypted());
        resSnapshot.setSourceDiskId(snapshot.getSourceDiskId());
        resSnapshot.setSourceDiskType(snapshot.getSourceDiskType());
        resSnapshot.setSourceDiskSize(Integer.valueOf(snapshot.getSourceDiskSize()));
        resSnapshot.setRegionId(regionId);
        return resSnapshot;
    }

    /**
     * 查询快照列表
     *
     * @param cloud 云
     * @return 快照列表
     */
    @Override
    public ResSnapshots describeSnapshots(CloudEntity cloud) {
        if (AliClient.getStatus()) {
            List<DescribeRegionsResponse.Region> regions = new ArrayList<>();
            DescribeRegionsResponse.Region regionInfo = new DescribeRegionsResponse.Region();
            regionInfo.setRegionId("cn-beijing");
            regions.add(regionInfo);
            ResRegions resRegions = new ResRegions(regions);
//            ResRegions resRegions = regionService.describeRegions(cloud);
            List<CompletionStage<List<SnapshotInfo>>> futures =
                    resRegions.getRegions().stream().map(region ->
                            CompletableFuture.supplyAsync(() -> {
                                //初始化
                                IAcsClient client = initClient(cloud, region.getRegionId());
                                //设置参数
                                DescribeSnapshotsRequest describeSnapshotsRequest = new DescribeSnapshotsRequest();
                                describeSnapshotsRequest.setRegionId(region.getRegionId());
                                describeSnapshotsRequest.setPageSize(DEFAULT_PAGE_SIZE);
                                AtomicInteger totalPage = new AtomicInteger(1);
                                List<DescribeSnapshotsResponse.Snapshot> snapshots = new ArrayList<>();
                                for (AtomicInteger pageNum = new AtomicInteger(1);
                                     pageNum.get() <= totalPage.get();
                                     pageNum.incrementAndGet()) {
                                    try {
                                        CompletableFuture.runAsync(() -> {
                                            try {
                                                describeSnapshotsRequest.setPageNumber(pageNum.get());
                                                DescribeSnapshotsResponse response =
                                                        client.getAcsResponse(describeSnapshotsRequest);
                                                snapshots.addAll(response.getSnapshots());
                                                totalPage.set((response.getTotalCount() / DEFAULT_PAGE_SIZE) + 1);
                                            } catch (Exception e) {
                                                ExceptionUtil.dealException(e, pageNum.get());
                                            }
                                        }).get(TIME_OUT_SECONDS, TimeUnit.SECONDS);
                                    } catch (Exception e) {
                                        break;
                                    }
                                }
                                return snapshots.stream()
                                        .map(snapshot -> convertSnapshot(snapshot, region.getRegionId()))
                                        .collect(toList());
                            })
                    ).collect(toList());
            List<SnapshotInfo> snapshots = CommonUtil.aggregateList(CommonUtil.joinRes(futures));
            return new ResSnapshots(snapshots);
        } else {
            List<SnapshotInfo> snapshots = AliSimulator.getAll(SnapshotInfo.class);
            return new ResSnapshots(snapshots);
        }
    }

    /**
     * 创建快照
     *
     * @param cloud          云
     * @param reqCreSnapshot 请求体
     */
    @Override
    public void createSnapshot(CloudEntity cloud, ReqCreSnapshot reqCreSnapshot) {
        if (AliClient.getStatus()) {
            //初始化
            IAcsClient client = initClient(cloud, reqCreSnapshot.getRegionId());
            //设置参数
            CreateSnapshotRequest createSnapshotRequest = new CreateSnapshotRequest();
            createSnapshotRequest.setRegionId(reqCreSnapshot.getRegionId());
            createSnapshotRequest.setDiskId(reqCreSnapshot.getDiskId());
            createSnapshotRequest.setSnapshotName(reqCreSnapshot.getSnapshotName());
            // 发起请求
            try {
                client.getAcsResponse(createSnapshotRequest);
            } catch (Exception e) {
                logger.error("createSnapshot error: {}", e.getMessage());
                ExceptionUtil.dealException(e);
            }
        }
    }
}
