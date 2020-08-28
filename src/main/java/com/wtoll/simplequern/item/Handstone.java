package com.wtoll.simplequern.item;

public interface Handstone {
    public default int grindLevel() {
        return 0;
    }

    public default HandstoneEnum property() {
        return HandstoneEnum.NONE;
    }
}
