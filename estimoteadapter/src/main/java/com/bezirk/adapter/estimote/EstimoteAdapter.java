package com.bezirk.adapter.estimote;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.bezirk.hardwareevents.beacon.Beacon;
import com.bezirk.hardwareevents.beacon.BeaconsDetectedEvent;
import com.bezirk.hardwareevents.beacon.GetBeaconAttributesEvent;
import com.bezirk.middleware.Bezirk;
import com.bezirk.middleware.addressing.ZirkEndPoint;
import com.bezirk.middleware.messages.Event;
import com.bezirk.middleware.messages.EventSet;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Nearable;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EstimoteAdapter {
    private static final String TAG = EstimoteAdapter.class.getName();
    private static final String ESTIMOTE_NEARABLE_PREFIX = "d0d3fa86-ca76-45ec-9bd9-6af4";
    private static final UUID ESTIMOTE_PROXIMITY_UUID = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
    private static final Region ALL_BEACONS = new Region("rid", null, null, null);

    private final BeaconManager beaconManager;
    private final Map<String, Nearable> beaconsSeen = new HashMap<>();
    private String scanId;
    private String xScanId;

    public enum Hardware {
        MANUFACTURER("estimote"),
        HARDWARE_NEARABLE("estimote.nearable"),
        HARDWARE_BEACON("estimote.beacon");

        private final String text;

        Hardware(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public EstimoteAdapter(final Bezirk bezirk, Context applicationContext) {
        beaconManager = new BeaconManager(applicationContext);
        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
            @Override
            public void onNearablesDiscovered(List<Nearable> nearables) {
                final List<EstimoteNearable> estimoteNearables = new ArrayList<>();

                for (Nearable nearable : nearables) {
                    estimoteNearables.add(new EstimoteNearable(
                            convertBatteryLevel(nearable.batteryLevel),
                            nearable.bootloaderVersion,
                            nearable.currentMotionStateDuration,
                            nearable.firmwareVersion,
                            nearable.hardwareVersion,
                            nearable.identifier,
                            nearable.isMoving,
                            nearable.lastMotionStateDuration,
                            convertOrientation(nearable.orientation),
                            nearable.rssi, nearable.xAcceleration,
                            nearable.yAcceleration, nearable.zAcceleration));
                    beaconsSeen.put(nearable.region.getProximityUUID().toString(), nearable);
                    Log.d(TAG, String.format("Estimote nearable detected: %s%n", nearable.toString()));
                }

                bezirk.sendEvent(new EstimoteNearablesDetectedEvent(estimoteNearables));
                Log.v(TAG, "Sent nearables detected event");
            }
        });

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<com.estimote.sdk.Beacon> list) {
                final List<Beacon> beacons = new ArrayList<>();
                final Set<Beacon> beaconsSeen = new HashSet<>();

                for (com.estimote.sdk.Beacon beacon : list) {
                    final UUID beaconUuid = beacon.getProximityUUID();
                    final String hardwareName;

                    if (beaconUuid.equals(ESTIMOTE_PROXIMITY_UUID)) {
                        hardwareName = Hardware.HARDWARE_BEACON.toString();
                    } else if (beaconUuid.toString().startsWith(ESTIMOTE_NEARABLE_PREFIX)) {
                        hardwareName = Hardware.HARDWARE_NEARABLE.toString();
                    } else {
                        hardwareName = "unknown";
                    }

                    final Beacon detectedBeacon = new Beacon(beaconUuid.toString(),
                            beacon.getMajor(), beacon.getMinor(), beacon.getRssi(),
                            hardwareName);
                    if (!beaconsSeen.contains(detectedBeacon)) {
                        beacons.add(detectedBeacon);
                        beaconsSeen.add(detectedBeacon);
                        Log.d(TAG, String.format("Beacon detected: %s%n", detectedBeacon.toString()));
                    }
                }

                bezirk.sendEvent(new BeaconsDetectedEvent(beacons));
                Log.v(TAG, "Sent beacons detected event");
            }
        });

        final EventSet beaconEvents = new EventSet(GetBeaconAttributesEvent.class);

        beaconEvents.setEventReceiver(new EventSet.EventReceiver() {
            @Override
            public void receiveEvent(Event event, ZirkEndPoint sender) {
                if (event instanceof GetBeaconAttributesEvent) {
                    GetBeaconAttributesEvent beaconAttributesEvent =
                            (GetBeaconAttributesEvent) event;

                    if (Hardware.HARDWARE_NEARABLE.toString().equalsIgnoreCase(
                            beaconAttributesEvent.getBeacon().getHardwareName()) &&
                            beaconsSeen.containsKey(beaconAttributesEvent.getBeacon().getId())) {
                        final Nearable requestedNearable = beaconsSeen.get(beaconAttributesEvent.getBeacon().getId());

                        final EstimoteNearable estimoteNearable =
                                new EstimoteNearable(
                                        convertBatteryLevel(requestedNearable.batteryLevel),
                                        requestedNearable.bootloaderVersion,
                                        requestedNearable.currentMotionStateDuration,
                                        requestedNearable.firmwareVersion,
                                        requestedNearable.hardwareVersion,
                                        requestedNearable.identifier,
                                        requestedNearable.isMoving,
                                        requestedNearable.lastMotionStateDuration,
                                        convertOrientation(requestedNearable.orientation),
                                        requestedNearable.rssi, requestedNearable.xAcceleration,
                                        requestedNearable.yAcceleration, requestedNearable.zAcceleration);

                        bezirk.sendEvent(sender, new EstimoteNearableAttributesEvent(estimoteNearable,
                                beaconAttributesEvent.getBeacon()));
                        Log.v(TAG, "Received estimote attribute request, sent attributes");
                    }
                }
            }
        });

        bezirk.subscribe(beaconEvents);
    }

    // This conversion is done in case estimote changes their orientation enum in a way
    // that changes the ordinal values assigned to each member.
    private EstimoteNearable.EstimoteBatteryLevel convertBatteryLevel(Nearable.BatteryLevel batteryLevel) {
        switch (batteryLevel) {
            case HIGH:
                return EstimoteNearable.EstimoteBatteryLevel.HIGH;
            case MEDIUM:
                return EstimoteNearable.EstimoteBatteryLevel.MEDIUM;
            case LOW:
                return EstimoteNearable.EstimoteBatteryLevel.LOW;
            case UNKNOWN:
                return EstimoteNearable.EstimoteBatteryLevel.UNKNOWN;
        }

        return EstimoteNearable.EstimoteBatteryLevel.UNKNOWN;
    }

    private EstimoteNearable.EstimoteOrientation convertOrientation(Nearable.Orientation orientation) {
        switch (orientation) {
            case HORIZONTAL:
                return EstimoteNearable.EstimoteOrientation.HORIZONTAL;
            case HORIZONTAL_UPSIDE_DOWN:
                return EstimoteNearable.EstimoteOrientation.HORIZONTAL_UPSIDE_DOWN;
            case LEFT_SIDE:
                return EstimoteNearable.EstimoteOrientation.LEFT_SIDE;
            case RIGHT_SIDE:
                return EstimoteNearable.EstimoteOrientation.RIGHT_SIDE;
            case UNKNOWN:
                return EstimoteNearable.EstimoteOrientation.UNKNOWN;
            case VERTICAL:
                return EstimoteNearable.EstimoteOrientation.VERTICAL;
            case VERTICAL_UPSIDE_DOWN:
                return EstimoteNearable.EstimoteOrientation.VERTICAL_UPSIDE_DOWN;
        }

        return EstimoteNearable.EstimoteOrientation.UNKNOWN;
    }

    public void start() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                scanId = beaconManager.startNearableDiscovery();
                xScanId = beaconManager.startEddystoneScanning();
                beaconManager.startRanging(ALL_BEACONS);
            }
        });
    }

    public void stop() {
        beaconManager.stopNearableDiscovery(scanId);
        beaconManager.stopEddystoneScanning(xScanId);
        beaconManager.stopRanging(ALL_BEACONS);
    }

    public void resume(Activity mainActivity) {
        SystemRequirementsChecker.checkWithDefaultDialogs(mainActivity);
    }
}
