package com.cmp.aliadapter.instance.model.res;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;

import java.util.List;

public class ResInstances {

    private List<DescribeInstancesResponse.Instance> instances;

    public ResInstances() {
    }

    public ResInstances(List<DescribeInstancesResponse.Instance> instances) {
        this.instances = instances;
    }

    public List<DescribeInstancesResponse.Instance> getInstances() {
        return instances;
    }

    public void setInstances(List<DescribeInstancesResponse.Instance> instances) {
        this.instances = instances;
    }
}
