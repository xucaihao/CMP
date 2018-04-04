package com.cmp.aliadapter.eip.service;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.AllocateEipAddressRequest;
import com.aliyuncs.vpc.model.v20160428.AllocateEipAddressResponse;
import com.aliyuncs.vpc.model.v20160428.DescribeEipAddressesRequest;
import com.aliyuncs.vpc.model.v20160428.DescribeEipAddressesResponse;
import com.cmp.aliadapter.common.AliClient;
import com.cmp.aliadapter.common.AliSimulator;
import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.common.ExceptionUtil;
import com.cmp.aliadapter.eip.modle.ResEipAddress;
import com.cmp.aliadapter.eip.modle.ResEipAddresses;
import com.cmp.aliadapter.eip.modle.req.ReqAllocateEipAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.cmp.aliadapter.common.AliClient.initClient;
import static com.cmp.aliadapter.common.Constance.DEFAULT_PAGE_SIZE;

@Service
public class EipAddressServiceImpl implements EipAddressService {

    private static final Logger logger = LoggerFactory.getLogger(EipAddressServiceImpl.class);

    /**
     * 查询弹性公网ip列表
     *
     * @param cloud    云
     * @param regionId 地域
     * @return 弹性公网ip列表
     */
    @Override
    public ResEipAddresses describeEipAddresses(CloudEntity cloud, String regionId) {
        if (AliClient.getStatus()) {
            //初始化
            IAcsClient client = initClient(cloud, regionId);
            //设置参数
            DescribeEipAddressesRequest describeEipAddresses = new DescribeEipAddressesRequest();
            describeEipAddresses.setRegionId(regionId);
            describeEipAddresses.setPageSize(DEFAULT_PAGE_SIZE);
            AtomicInteger totalPage = new AtomicInteger(1);
            List<DescribeEipAddressesResponse.EipAddress> eipAddresses = new ArrayList<>();
            for (AtomicInteger pageNum = new AtomicInteger(1); pageNum.get() <= totalPage.get(); pageNum.incrementAndGet()) {
                CompletableFuture.runAsync(() -> {
                    try {
                        describeEipAddresses.setPageNumber(pageNum.get());
                        DescribeEipAddressesResponse response = client.getAcsResponse(describeEipAddresses);
                        eipAddresses.addAll(response.getEipAddresses());
                        totalPage.set((response.getTotalCount() / DEFAULT_PAGE_SIZE) + 1);
                    } catch (Exception e) {
                        ExceptionUtil.dealException(e, pageNum.get());
                    }
                });
            }
            return new ResEipAddresses(eipAddresses);
        } else {
            List<DescribeEipAddressesResponse.EipAddress> eipAddresses =
                    AliSimulator.getAll(DescribeEipAddressesResponse.EipAddress.class);
            return new ResEipAddresses(eipAddresses);
        }
    }

    /**
     * 查询指定弹性公网ip
     *
     * @param cloud        云
     * @param regionId     地域
     * @param allocationId 弹性公网ip的id
     * @return 指定弹性公网ip
     */
    @Override
    public ResEipAddress describeEipAddressAttribute(CloudEntity cloud, String regionId, String allocationId) {
        if (AliClient.getStatus()) {
            //初始化
            IAcsClient client = initClient(cloud, regionId);
            //设置参数
            DescribeEipAddressesRequest describeEipAddresses = new DescribeEipAddressesRequest();
            describeEipAddresses.setRegionId(regionId);
            describeEipAddresses.setAllocationId(allocationId);
            DescribeEipAddressesResponse response;
            // 发起请求
            try {
                response = client.getAcsResponse(describeEipAddresses);
                return new ResEipAddress(response.getEipAddresses().get(0));
            } catch (Exception e) {
                logger.error("describeEipAddressAttribute error: {}", e.getMessage());
                ExceptionUtil.dealException(e);
                return null;
            }
        } else {
            DescribeEipAddressesResponse.EipAddress eipAddress =
                    AliSimulator.get(DescribeEipAddressesResponse.EipAddress.class, allocationId);
            return new ResEipAddress(eipAddress);
        }
    }

    /**
     * 创建弹性公网ip
     *
     * @param cloud                 云
     * @param reqAllocateEipAddress 请求体
     * @return 操作结果
     */
    @Override
    public ResEipAddress AllocateEipAddress(CloudEntity cloud, ReqAllocateEipAddress reqAllocateEipAddress) {
        if (AliClient.getStatus()) {
            String regionId = reqAllocateEipAddress.getRegionId();
            //初始化
            IAcsClient client = initClient(cloud, regionId);
            //设置参数
            AllocateEipAddressRequest allocateEipAddress = new AllocateEipAddressRequest();
            allocateEipAddress.setRegionId(regionId);
            Optional.ofNullable(reqAllocateEipAddress.getBandwidth())
                    .ifPresent(allocateEipAddress::setBandwidth);
            Optional.ofNullable(reqAllocateEipAddress.getInternetChargeType())
                    .ifPresent(allocateEipAddress::setInstanceChargeType);
            // 发起请求
            try {
                AllocateEipAddressResponse response = client.getAcsResponse(allocateEipAddress);
                DescribeEipAddressesResponse.EipAddress eipAddress = new DescribeEipAddressesResponse.EipAddress();
                eipAddress.setAllocationId(response.getAllocationId());
                eipAddress.setIpAddress(response.getEipAddress());
                return new ResEipAddress(eipAddress);
            } catch (Exception e) {
                logger.error("AllocateEipAddress error: {}", e.getMessage());
                ExceptionUtil.dealException(e);
                return null;
            }
        } else {
            Map<String, Object> values = new HashMap<>(16);
            values.put("regionId", reqAllocateEipAddress.getRegionId());
            Random r = new Random();
            String id = "eip-" + UUID.randomUUID().toString();
            values.put("allocationId", id);
            String ip = r.nextInt(200) + "." + r.nextInt(200) + "." + r.nextInt(255) + "." + r.nextInt(255);
            values.put("ipAddress", ip);
            Optional.ofNullable(reqAllocateEipAddress.getBandwidth())
                    .ifPresent(bandWidth -> values.put("bandwidth", bandWidth));
            Optional.ofNullable(reqAllocateEipAddress.getInternetChargeType())
                    .ifPresent(internetChargeType -> values.put("internetChargeType", internetChargeType));
            DescribeEipAddressesResponse.EipAddress eipAddress =
                    AliSimulator.create(DescribeEipAddressesResponse.EipAddress.class, id, values);
            return new ResEipAddress(eipAddress);
        }
    }


}
