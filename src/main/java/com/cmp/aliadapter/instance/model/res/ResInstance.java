package com.cmp.aliadapter.instance.model.res;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;

public class ResInstance {

    private DescribeInstancesResponse.Instance instance;

    public ResInstance() {
    }

    public ResInstance(DescribeInstancesResponse.Instance instance) {
        this.instance = instance;
    }

    public DescribeInstancesResponse.Instance getInstance() {
        return instance;
    }

    public void setInstance(DescribeInstancesResponse.Instance instance) {
        this.instance = instance;
    }
}
