package com.bezirk.adapter.estimote;

import com.bezirk.middleware.messages.Event;

import java.util.List;

public class EstimoteNearablesDetectedEvent extends Event {
    private final List<EstimoteNearable> estimoteNearables;

    public EstimoteNearablesDetectedEvent(List<EstimoteNearable> estimoteNearables) {
        this.estimoteNearables = estimoteNearables;
    }

    public List<EstimoteNearable> getNearables() {
        return estimoteNearables;
    }
}
