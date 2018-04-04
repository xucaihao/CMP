package com.cmp.aliadapter.eip.modle;

import com.aliyuncs.vpc.model.v20160428.DescribeEipAddressesResponse;

public class ResEipAddress {

    private DescribeEipAddressesResponse.EipAddress eipAddress;

    public ResEipAddress() {
    }

    public ResEipAddress(DescribeEipAddressesResponse.EipAddress eipAddress) {
        this.eipAddress = eipAddress;
    }

    public DescribeEipAddressesResponse.EipAddress getEipAddress() {
        return eipAddress;
    }

    public void setEipAddress(DescribeEipAddressesResponse.EipAddress eipAddress) {
        this.eipAddress = eipAddress;
    }
}
