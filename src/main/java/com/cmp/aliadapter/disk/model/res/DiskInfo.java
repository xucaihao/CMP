package com.cmp.aliadapter.disk.model.res;

public class DiskInfo {

    private String diskId;
    private String diskName;
    private String regionId;
    private String description;

    /**
     * 磁盘类型，
     * system：系统盘
     * data：数据盘
     */
    private String type;

    /**
     * 磁盘种类，
     * cloud：普通云盘
     * cloud_efficiency：高效云盘
     * cloud_ssd：SSD云盘
     * ephemeral_ssd: 本地 SSD 盘
     * ephemeral: 本地磁盘
     */
    private String category;

    /**
     * 是否为加密磁盘
     * true: 是加密磁盘
     * false：不是加密磁盘
     */
    private Boolean encrypted;

    /**
     * 磁盘大小，单位 GB
     */
    private int size;

    /**
     * 磁盘状态。
     * In_use
     * Available
     * Attaching
     * Detaching
     * Creating
     * ReIniting
     */
    private String status;

    /**
     * 磁盘挂载的实例 ID。只有在 Status 为 In_use 时才有值，其他状态为空。
     */
    private String instanceId;

    /**
     * 创建时间
     */
    private String creationTime;
    /**
     * 付费方式
     * PrePaid：预付费，即包年包月。
     * PostPaid：后付费，即按量付费。
     */
    private String diskChargeType;

    private Boolean portable;

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getDiskChargeType() {
        return diskChargeType;
    }

    public void setDiskChargeType(String diskChargeType) {
        this.diskChargeType = diskChargeType;
    }

    public Boolean getPortable() {
        return portable;
    }

    public void setPortable(Boolean portable) {
        this.portable = portable;
    }
}
