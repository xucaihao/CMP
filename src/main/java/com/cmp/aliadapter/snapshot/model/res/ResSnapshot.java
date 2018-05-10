package com.cmp.aliadapter.snapshot.model.res;

public class ResSnapshot {

    private SnapshotInfo snapshot;

    public ResSnapshot() {
    }

    public ResSnapshot(SnapshotInfo snapshot) {
        this.snapshot = snapshot;
    }

    public SnapshotInfo getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(SnapshotInfo snapshot) {
        this.snapshot = snapshot;
    }
}
