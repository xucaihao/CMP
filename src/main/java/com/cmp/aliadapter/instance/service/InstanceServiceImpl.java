package com.cmp.aliadapter.instance.service;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.cmp.aliadapter.common.*;
import com.cmp.aliadapter.instance.model.req.ReqCloseInstance;
import com.cmp.aliadapter.instance.model.req.ReqModifyInstance;
import com.cmp.aliadapter.instance.model.req.ReqStartInstance;
import com.cmp.aliadapter.instance.model.res.ResInstance;
import com.cmp.aliadapter.instance.model.res.ResInstances;
import com.cmp.aliadapter.region.model.res.ResRegions;
import com.cmp.aliadapter.region.service.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.cmp.aliadapter.common.AliClient.initClient;
import static com.cmp.aliadapter.common.Constance.DEFAULT_PAGE_SIZE;
import static com.cmp.aliadapter.common.Constance.TIME_OUT_SECONDS;
import static java.util.stream.Collectors.toList;

@Service
public class InstanceServiceImpl implements InstanceService {

    private static final Logger logger = LoggerFactory.getLogger(InstanceServiceImpl.class);

    @Autowired
    private RegionService regionService;

    /**
     * 查询实例列表
     *
     * @param cloud 云
     * @return 实例列表
     */
    @Override
    public ResInstances describeInstances(CloudEntity cloud) {
        if (AliClient.getStatus()) {
            List<DescribeRegionsResponse.Region> regions = new ArrayList<>();
            DescribeRegionsResponse.Region regionInfo = new DescribeRegionsResponse.Region();
            regionInfo.setRegionId("cn-beijing");
            regions.add(regionInfo);
            ResRegions resRegions = new ResRegions(regions);
//            ResRegions resRegions = regionService.describeRegions(cloud);
            List<CompletionStage<List<DescribeInstancesResponse.Instance>>> futures =
                    resRegions.getRegions().stream().map(region ->
                            CompletableFuture.supplyAsync(() -> {
                                //初始化
                                IAcsClient client = initClient(cloud, region.getRegionId());
                                //设置参数
                                DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
                                describeInstancesRequest.setRegionId(region.getRegionId());
                                describeInstancesRequest.setPageSize(DEFAULT_PAGE_SIZE);
                                AtomicInteger totalPage = new AtomicInteger(1);
                                List<DescribeInstancesResponse.Instance> instances = new ArrayList<>();
                                for (AtomicInteger pageNum = new AtomicInteger(1);
                                     pageNum.get() <= totalPage.get();
                                     pageNum.incrementAndGet()) {
                                    try {
                                        CompletableFuture.runAsync(() -> {
                                            try {
                                                describeInstancesRequest.setPageNumber(pageNum.get());
                                                DescribeInstancesResponse response =
                                                        client.getAcsResponse(describeInstancesRequest);
                                                response.getInstances().forEach(vo -> {
                                                    List<String> privateIps = vo.getNetworkInterfaces().stream()
                                                            .map(DescribeInstancesResponse.Instance.NetworkInterface::getPrimaryIpAddress)
                                                            .collect(toList());
                                                    vo.setInnerIpAddress(privateIps);
                                                    String createdTime = vo.getCreationTime()
                                                            .replace("T", " ")
                                                            .replace("Z", " ");
                                                    vo.setCreationTime(createdTime);
                                                    String expiredTime = vo.getExpiredTime()
                                                            .replace("T", " ")
                                                            .replace("Z", " ");
                                                    vo.setExpiredTime(expiredTime);
                                                });
                                                instances.addAll(response.getInstances());
                                                totalPage.set((response.getTotalCount() / DEFAULT_PAGE_SIZE) + 1);
                                            } catch (Exception e) {
                                                ExceptionUtil.dealException(e, pageNum.get());
                                            }
                                        }).get(TIME_OUT_SECONDS, TimeUnit.SECONDS);
                                    } catch (Exception e) {
                                        break;
                                    }
                                }
                                return instances;
                            })
                    ).collect(toList());
            List<DescribeInstancesResponse.Instance> instances = CommonUtil.aggregateList(CommonUtil.joinRes(futures));
            return new ResInstances(instances);
        } else {
            List<DescribeInstancesResponse.Instance> instances =
                    AliSimulator.getAll(DescribeInstancesResponse.Instance.class);
            return new ResInstances(instances);
        }
    }

    /**
     * 查询指定实例
     *
     * @param cloud      云
     * @param regionId   地域
     * @param instanceId 实例id
     * @return 指定实例
     */
    @Override
    public ResInstance describeInstance(CloudEntity cloud, String regionId, String instanceId) {
        if (AliClient.getStatus()) {
            //初始化
            IAcsClient client = initClient(cloud, regionId);
            //设置参数
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setRegionId(regionId);
            describeInstancesRequest.setInstanceIds("[\"" + instanceId + "\"]");

            // 发起请求
            try {
                DescribeInstancesResponse response = client.getAcsResponse(describeInstancesRequest);
                return new ResInstance(response.getInstances().get(0));
            } catch (Exception e) {
                logger.error("describeInstance error: {}", e.getMessage());
                ExceptionUtil.dealException(e);
                return null;
            }
        } else {
            DescribeInstancesResponse.Instance instance =
                    AliSimulator.get(DescribeInstancesResponse.Instance.class, instanceId);
            return new ResInstance(instance);
        }
    }

    /**
     * 关闭实例
     *
     * @param cloud            云（用户提供ak、sk）
     * @param reqCloseInstance 请求体
     */
    @Override
    public void closeInstance(CloudEntity cloud, ReqCloseInstance reqCloseInstance) {
        if (AliClient.getStatus()) {
            //初始化
            IAcsClient client = initClient(cloud, reqCloseInstance.getRegionId());
            //设置参数
            StopInstanceRequest stopInstanceRequest = new StopInstanceRequest();
            stopInstanceRequest.setRegionId(reqCloseInstance.getRegionId());
            stopInstanceRequest.setInstanceId(reqCloseInstance.getInstanceId());
            stopInstanceRequest.setConfirmStop(reqCloseInstance.isForceStop());
            // 发起请求
            try {
                client.getAcsResponse(stopInstanceRequest);
            } catch (Exception e) {
                logger.error("closeInstance error: {}", e.getMessage());
                ExceptionUtil.dealException(e);
            }
        } else {
            Map<String, Object> values = new HashMap<>(16);
            values.put("status", "stopped");
            AliSimulator.modify(DescribeInstancesResponse.Instance.class, reqCloseInstance.getInstanceId(), values);
        }
    }

    /**
     * 启动实例
     *
     * @param cloud            云（用户提供ak、sk）
     * @param reqStartInstance 请求体
     */
    @Override
    public void startInstance(CloudEntity cloud, ReqStartInstance reqStartInstance) {
        if (AliClient.getStatus()) {
            //初始化
            IAcsClient client = initClient(cloud, reqStartInstance.getRegionId());
            //设置参数
            StartInstanceRequest startInstanceRequest = new StartInstanceRequest();
            startInstanceRequest.setRegionId(reqStartInstance.getRegionId());
            startInstanceRequest.setInstanceId(reqStartInstance.getInstanceId());
            // 发起请求
            try {
                client.getAcsResponse(startInstanceRequest);
            } catch (Exception e) {
                logger.error("startInstance error: {}", e.getMessage());
                ExceptionUtil.dealException(e);
            }
        } else {
            Map<String, Object> values = new HashMap<>(16);
            values.put("status", "running");
            AliSimulator.modify(DescribeInstancesResponse.Instance.class, reqStartInstance.getInstanceId(), values);
        }
    }

    /**
     * 修改实例名称
     *
     * @param cloud             云（用户提供ak、sk）
     * @param reqModifyInstance 请求体
     */
    @Override
    public void modifyInstanceName(CloudEntity cloud, ReqModifyInstance reqModifyInstance) {
        if (AliClient.getStatus()) {
            //初始化
            IAcsClient client = initClient(cloud, reqModifyInstance.getRegionId());
            //设置参数
            ModifyInstanceAttributeRequest modifyInstanceAttributeRequest =
                    new ModifyInstanceAttributeRequest();
            modifyInstanceAttributeRequest.setRegionId(reqModifyInstance.getRegionId());
            modifyInstanceAttributeRequest.setInstanceId(reqModifyInstance.getInstanceId());
            modifyInstanceAttributeRequest.setInstanceName(reqModifyInstance.getInstanceName());
            // 发起请求
            try {
                client.getAcsResponse(modifyInstanceAttributeRequest);
            } catch (Exception e) {
                logger.error("modifyInstanceName error: {}", e.getMessage());
                ExceptionUtil.dealException(e);
            }
        } else {
            Map<String, Object> values = new HashMap<>(16);
            values.put("instanceName", reqModifyInstance.getInstanceName());
            AliSimulator.modify(DescribeInstancesResponse.Instance.class, reqModifyInstance.getInstanceId(), values);
        }
    }

    /**
     * 重置实例密码
     *
     * @param cloud             云（用户提供ak、sk）
     * @param reqModifyInstance 请求体
     */
    @Override
    public void resetInstancesPassword(CloudEntity cloud, ReqModifyInstance reqModifyInstance) {
        if (AliClient.getStatus()) {
            //初始化
            IAcsClient client = initClient(cloud, reqModifyInstance.getRegionId());
            //设置参数
            ModifyInstanceAttributeRequest modifyInstanceAttributeRequest =
                    new ModifyInstanceAttributeRequest();
            modifyInstanceAttributeRequest.setRegionId(reqModifyInstance.getRegionId());
            modifyInstanceAttributeRequest.setInstanceId(reqModifyInstance.getInstanceId());
            modifyInstanceAttributeRequest.setPassword(reqModifyInstance.getPassword());
            // 发起请求
            try {
                client.getAcsResponse(modifyInstanceAttributeRequest);
            } catch (Exception e) {
                logger.error("resetInstancesPassword error: {}", e.getMessage());
                ExceptionUtil.dealException(e);
            }
        }
    }

}
