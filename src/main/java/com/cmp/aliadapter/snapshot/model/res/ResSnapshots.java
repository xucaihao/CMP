package com.cmp.aliadapter.snapshot.model.res;

import java.util.List;

public class ResSnapshots {

    private List<SnapshotInfo> snapshots;

    public ResSnapshots() {
    }

    public ResSnapshots(List<SnapshotInfo> snapshots) {
        this.snapshots = snapshots;
    }

    public List<SnapshotInfo> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<SnapshotInfo> snapshots) {
        this.snapshots = snapshots;
    }
}
