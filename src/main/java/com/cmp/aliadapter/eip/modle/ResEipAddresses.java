package com.cmp.aliadapter.eip.modle;

import com.aliyuncs.vpc.model.v20160428.DescribeEipAddressesResponse;

import java.util.List;

public class ResEipAddresses {

    private List<DescribeEipAddressesResponse.EipAddress> eipAddresses;

    public ResEipAddresses() {
    }

    public ResEipAddresses(List<DescribeEipAddressesResponse.EipAddress> eipAddresses) {
        this.eipAddresses = eipAddresses;
    }

    public List<DescribeEipAddressesResponse.EipAddress> getEipAddresses() {
        return eipAddresses;
    }

    public void setEipAddresses(List<DescribeEipAddressesResponse.EipAddress> eipAddresses) {
        this.eipAddresses = eipAddresses;
    }
}
