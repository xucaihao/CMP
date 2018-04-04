package com.cmp.aliadapter.eip.modle;

import com.aliyuncs.vpc.model.v20160428.DescribeEipAddressesResponse;

import java.util.List;

public class EipAddressInfo {

    private String regionId;
    private String ipAddress;
    /**
     * id
     */
    private String allocationId;
    private String status;
    private String instanceId;
    private String bandwidth;
    private String internetChargeType;
    private String allocationTime;
    private String instanceType;
    private String chargeType;
    private String expiredTime;
    private String name;
    private String descritpion;
    private String bandwidthPackageId;
    private List<DescribeEipAddressesResponse.EipAddress.LockReason> operationLocks;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
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

    public String getAllocationTime() {
        return allocationTime;
    }

    public void setAllocationTime(String allocationTime) {
        this.allocationTime = allocationTime;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescritpion() {
        return descritpion;
    }

    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
    }

    public String getBandwidthPackageId() {
        return bandwidthPackageId;
    }

    public void setBandwidthPackageId(String bandwidthPackageId) {
        this.bandwidthPackageId = bandwidthPackageId;
    }

    public List<DescribeEipAddressesResponse.EipAddress.LockReason> getOperationLocks() {
        return operationLocks;
    }

    public void setOperationLocks(List<DescribeEipAddressesResponse.EipAddress.LockReason> operationLocks) {
        this.operationLocks = operationLocks;
    }
}
