package com.bezirk.adapter.estimote;

public class EstimoteNearable {
    private final EstimoteBatteryLevel batteryLevel;
    private final String bootloaderVersion;
    private final long currentMotionStateDuration;
    private final String firmwareVersion;
    private final String hardwareVersion;
    private final String identifier;
    private final boolean isMoving;
    private final long lastMotionStateDuration;
    private final EstimoteOrientation orientation;
    private final int rssi;
    private final double xAcceleration;
    private final double yAcceleration;
    private final double zAcceleration;

    public EstimoteNearable(EstimoteBatteryLevel batteryLevel,
                                           String bootloaderVersion, long currentMotionStateDuration,
                                           String firmwareVersion, String hardwareVersion,
                                           String identifier, boolean isMoving,
                                           long lastMotionStateDuration, EstimoteOrientation orientation,
                                           int rssi, double xAcceleration,
                                           double yAcceleration, double zAcceleration) {
        this.batteryLevel = batteryLevel;
        this.bootloaderVersion = bootloaderVersion;
        this.currentMotionStateDuration = currentMotionStateDuration;
        this.firmwareVersion = firmwareVersion;
        this.hardwareVersion = hardwareVersion;
        this.identifier = identifier;
        this.isMoving = isMoving;
        this.lastMotionStateDuration = lastMotionStateDuration;
        this.orientation = orientation;
        this.rssi = rssi;
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
        this.zAcceleration = zAcceleration;
    }

    public EstimoteBatteryLevel getBatteryLevel() {
        return batteryLevel;
    }

    public String getBootloaderVersion() {
        return bootloaderVersion;
    }

    public long getCurrentMotionStateDuration() {
        return currentMotionStateDuration;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public long getLastMotionStateDuration() {
        return lastMotionStateDuration;
    }

    public EstimoteOrientation getOrientation() {
        return orientation;
    }

    public int getRssi() {
        return rssi;
    }

    public double getXAcceleration() {
        return xAcceleration;
    }

    public double getYAcceleration() {
        return yAcceleration;
    }

    public double getZAcceleration() {
        return zAcceleration;
    }

    public enum EstimoteOrientation {
        HORIZONTAL, HORIZONTAL_UPSIDE_DOWN, LEFT_SIDE, RIGHT_SIDE, UNKNOWN, VERTICAL,
        VERTICAL_UPSIDE_DOWN
    }

    public enum EstimoteType {
        ALL("all"),
        BAG("bag"),
        BED("bed"),
        BIKE("bike"),
        CAR("car"),
        CHAIR("chair"),
        DOG("dog"),
        DOOR("door"),
        FRIDGE("fridge"),
        GENERIC("generic"),
        SHOE("shoe"),
        UNKNOWN("unknown");

        private final String text;

        EstimoteType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum EstimoteBatteryLevel {
        HIGH, MEDIUM, LOW, UNKNOWN
    }
}
