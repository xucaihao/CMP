package com.cmp.aliadapter.disk.model.res;

import java.util.List;

public class ResDisks {

    private List<DiskInfo> disks;

    public ResDisks() {
    }

    public ResDisks(List<DiskInfo> disks) {
        this.disks = disks;
    }

    public List<DiskInfo> getDisks() {
        return disks;
    }

    public void setDisks(List<DiskInfo> disks) {
        this.disks = disks;
    }
}
