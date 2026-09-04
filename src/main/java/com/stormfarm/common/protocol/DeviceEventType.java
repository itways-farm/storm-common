package com.stormfarm.common.protocol;

/**
 * Event names carried in the {@code type} field of the envelope published on
 * {@link RedisChannels#DEVICE_EVENTS} and forwarded verbatim as SSE event
 * names to the web console.
 */
public final class DeviceEventType {
    public static final String UPDATE = "device.update";
    public static final String DELETE = "device.delete";
    public static final String FROZEN = "device.frozen";

    private DeviceEventType() {}
}
