package com.stormfarm.common.protocol;

/**
 * Redis pub/sub channels shared between the services.
 *
 * <ul>
 *   <li>{@link #DEVICE_EVENTS}: device state changes, published by storm-core and storm-device-detector,
 *       fanned out to SSE clients by storm-device-detector</li>
 *   <li>{@link #BRIDGE_RESPONSES}: correlated bridge RESPONSE messages relayed by storm-device-detector
 *       to storm-core (Redis rather than Kafka because screenshots exceed Kafka's 1 MB default)</li>
 *   <li>{@link #STREAM_REQUEST}: start/stop stream requests consumed by storm-core</li>
 * </ul>
 */
public final class RedisChannels {
    public static final String DEVICE_EVENTS = "stormfarm:device-events";
    public static final String BRIDGE_RESPONSES = "stormfarm:bridge-responses";
    public static final String STREAM_REQUEST = "stream.request";

    private RedisChannels() {}
}
