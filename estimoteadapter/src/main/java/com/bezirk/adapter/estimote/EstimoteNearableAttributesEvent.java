package com.bezirk.adapter.estimote;

import com.bezirk.hardwareevents.beacon.Beacon;
import com.bezirk.middleware.messages.Event;

public class EstimoteNearableAttributesEvent extends Event {
    private final EstimoteNearable estimoteNearable;
    private final Beacon beacon;

    public EstimoteNearableAttributesEvent(EstimoteNearable estimoteNearable, Beacon beacon) {
        this.estimoteNearable = estimoteNearable;
        this.beacon = beacon;
    }

    public EstimoteNearable getEstimoteNearable() {
        return estimoteNearable;
    }

    public Beacon getBeacon() {
        return beacon;
    }
}
