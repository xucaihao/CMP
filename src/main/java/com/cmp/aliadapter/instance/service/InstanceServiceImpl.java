package com.cmp.aliadapter.instance.service;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.cmp.aliadapter.common.AliClient;
import com.cmp.aliadapter.common.AliSimulator;
import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.common.ExceptionUtil;
import com.cmp.aliadapter.instance.model.res.ResInstance;
import com.cmp.aliadapter.instance.model.res.ResInstances;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.cmp.aliadapter.common.AliClient.initClient;
import static com.cmp.aliadapter.common.Constance.DEFAULT_PAGE_SIZE;

@Service
public class InstanceServiceImpl implements InstanceService {

    private static final Logger logger = LoggerFactory.getLogger(InstanceServiceImpl.class);

    /**
     * 查询实例列表
     *
     * @param cloud    云
     * @param regionId 地域
     * @return 实例列表
     */
    @Override
    public ResInstances describeInstances(CloudEntity cloud, String regionId) {
        if (AliClient.getStatus()) {
            //初始化
            IAcsClient client = initClient(cloud, regionId);
            //设置参数
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setRegionId(regionId);
            describeInstancesRequest.setPageSize(DEFAULT_PAGE_SIZE);
            AtomicInteger totalPage = new AtomicInteger(1);
            List<DescribeInstancesResponse.Instance> instances = new ArrayList<>();
            for (AtomicInteger pageNum = new AtomicInteger(1); pageNum.get() <= totalPage.get(); pageNum.incrementAndGet()) {
                CompletableFuture.runAsync(() -> {
                    try {
                        describeInstancesRequest.setPageNumber(pageNum.get());
                        DescribeInstancesResponse response =
                                client.getAcsResponse(describeInstancesRequest);
                        instances.addAll(response.getInstances());
                        totalPage.set((response.getTotalCount() / DEFAULT_PAGE_SIZE) + 1);
                    } catch (Exception e) {
                        ExceptionUtil.dealException(e, pageNum.get());
                    }
                });
            }
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


}
