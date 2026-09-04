package com.stormfarm.common.protocol;

/**
 * Kafka topics shared between the services. Producers and consumers reference
 * these constants rather than repeating the string.
 *
 * <ul>
 *   <li>{@link #DEVICE_COMMANDS}: storm-core to storm-device-detector to bridge (JSON command, key = device serial)</li>
 *   <li>{@link #SCRCPY_STATUS}: storm-device-detector to storm-stream (bridge stream status, key = device id)</li>
 *   <li>{@link #STREAM_REQUEST}: legacy start/stop stream request consumed by storm-core; superseded by the
 *       HTTP call to {@code /internal/stream} but the listener still exists</li>
 * </ul>
 */
public final class KafkaTopics {
    public static final String DEVICE_COMMANDS = "device.commands";
    public static final String SCRCPY_STATUS = "scrcpy.status";
    public static final String STREAM_REQUEST = "stream.request";

    private KafkaTopics() {}
}
