package com.cmp.aliadapter.region.service;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;
import com.cmp.aliadapter.common.AliClient;
import com.cmp.aliadapter.common.CloudEntity;
import com.cmp.aliadapter.common.ExceptionUtil;
import com.cmp.aliadapter.region.model.res.ResRegions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cmp.aliadapter.common.AliClient.initClient;

@Service
public class RegionServiceImpl implements RegionService {

    private static final Logger logger = LoggerFactory.getLogger(RegionServiceImpl.class);

    /**
     * 查询所有地域列表
     *
     * @param cloud 云
     * @return 所有地域列表
     */
    @Override
    public ResRegions describeRegions(CloudEntity cloud) {
        if (AliClient.getStatus()) {
            //初始化
            IAcsClient client = initClient(cloud, "cn-hangzhou");
            //设置参数
            DescribeRegionsRequest describeRegions = new DescribeRegionsRequest();
            DescribeRegionsResponse response;
            // 发起请求
            try {
                response = client.getAcsResponse(describeRegions);
                return new ResRegions(response.getRegions());
            } catch (Exception e) {
                logger.error("describeEipAddressAttribute error: {}", e.getMessage());
                ExceptionUtil.dealException(e);
                return null;
            }
        } else {
            List<DescribeRegionsResponse.Region> regions = new ArrayList<>();
            DescribeRegionsResponse.Region region0 = new DescribeRegionsResponse.Region();
            region0.setRegionId("cn-qingdao");
            region0.setLocalName("华北 1");
            regions.add(region0);

            DescribeRegionsResponse.Region region1 = new DescribeRegionsResponse.Region();
            region1.setRegionId("cn-beijing");
            region1.setLocalName("华北 2");
            regions.add(region1);

            DescribeRegionsResponse.Region region2 = new DescribeRegionsResponse.Region();
            region2.setRegionId("cn-zhangjiakou");
            region2.setLocalName("华北 3");
            regions.add(region2);

            DescribeRegionsResponse.Region region3 = new DescribeRegionsResponse.Region();
            region3.setRegionId("cn-huhehaote");
            region3.setLocalName("华北 5");
            regions.add(region3);

            DescribeRegionsResponse.Region region4 = new DescribeRegionsResponse.Region();
            region4.setRegionId("cn-hangzhou");
            region4.setLocalName("华东 1");
            regions.add(region4);

            DescribeRegionsResponse.Region region5 = new DescribeRegionsResponse.Region();
            region5.setRegionId("cn-shanghai");
            region5.setLocalName("华东 2");
            regions.add(region5);

            DescribeRegionsResponse.Region region6 = new DescribeRegionsResponse.Region();
            region6.setRegionId("cn-shenzhen");
            region6.setLocalName("华南 1");
            regions.add(region6);

            DescribeRegionsResponse.Region region7 = new DescribeRegionsResponse.Region();
            region7.setRegionId("cn-hongkong");
            region7.setLocalName("香港");
            regions.add(region7);

            return new ResRegions(regions);
        }
    }
}
