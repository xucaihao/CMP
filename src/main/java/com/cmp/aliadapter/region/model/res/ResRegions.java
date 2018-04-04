package com.cmp.aliadapter.region.model.res;

import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;

import java.util.List;

public class ResRegions {

    private List<DescribeRegionsResponse.Region> regions;

    public ResRegions() {
    }

    public ResRegions(List<DescribeRegionsResponse.Region> regions) {
        this.regions = regions;
    }

    public List<DescribeRegionsResponse.Region> getRegions() {
        return regions;
    }

    public void setRegions(List<DescribeRegionsResponse.Region> regions) {
        this.regions = regions;
    }
}
