package com.cmp.aliadapter.snapshot.model.res;

public class SnapshotInfo {

    private String snapshotId;
    private String snapshotName;
    /**
     * normal 已创建
     * creating 创建中
     * rollbacking 回滚中
     */
    private String status;
    /**
     * 快照创建进度百分比
     */
    private int percent;
    private String creationTime;
    /**
     * 是否加密
     */
    private Boolean encrypted;
    /**
     * 源硬盘id
     */
    private String sourceDiskId;
    /**
     * 源硬盘类型
     */
    private String sourceDiskType;
    /**
     * 源硬盘大小
     */
    private int sourceDiskSize;

    private String regionId;

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public Boolean getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }

    public String getSourceDiskId() {
        return sourceDiskId;
    }

    public void setSourceDiskId(String sourceDiskId) {
        this.sourceDiskId = sourceDiskId;
    }

    public String getSourceDiskType() {
        return sourceDiskType;
    }

    public void setSourceDiskType(String sourceDiskType) {
        this.sourceDiskType = sourceDiskType;
    }

    public int getSourceDiskSize() {
        return sourceDiskSize;
    }

    public void setSourceDiskSize(int sourceDiskSize) {
        this.sourceDiskSize = sourceDiskSize;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
