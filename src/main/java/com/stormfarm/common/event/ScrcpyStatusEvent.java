package com.stormfarm.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * In-process event carrying a bridge stream status update
 * (PREPARING, STARTING_SERVER, HANDSHAKE, STREAMING, ERROR, ...).
 * Published by storm-device-detector when the bridge reports it and by
 * storm-stream when the same report arrives over Kafka.
 */
public class ScrcpyStatusEvent extends ApplicationEvent {
    private final Long deviceId;
    private final String status;
    private final String message;

    public ScrcpyStatusEvent(Object source, Long deviceId, String status, String message) {
        super(source);
        this.deviceId = deviceId;
        this.status = status;
        this.message = message;
    }

    public Long getDeviceId() { return deviceId; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
}
