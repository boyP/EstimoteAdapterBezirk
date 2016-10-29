package com.bezirk.candidcamera.events;

import com.bezirk.middleware.messages.Event;

/**
 * Created by pratikprakash on 10/29/16.
 */

public class VicinityEvent extends Event {

    private final boolean inVicinity;

    public VicinityEvent(boolean inVicinity) {
        this.inVicinity = inVicinity;
    }

    public boolean isInVicinity() {
        return this.inVicinity;
    }
}
