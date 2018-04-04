package com.cmp.aliadapter.eip.modle.req;

public class ReqAllocateEipAddress {

    /**
     * 申请的弹性公网 IP 所在的地域
     */
    private String regionId;

    /**
     * 弹性公网 IP 的限速，如果不指定，默认为 5 Mbps
     */
    private String bandwidth;

    /**
     * 按量付费计费方式（PayByBandwidth/PayByTraffic），默认为 PayByBandwidth
     */
    private String internetChargeType;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getInternetChargeType() {
        return internetChargeType;
    }

    public void setInternetChargeType(String internetChargeType) {
        this.internetChargeType = internetChargeType;
    }
}
