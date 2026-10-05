package com.stormfarm.common.protocol;

import java.util.Set;

/**
 * Capabilities a desktop agent may advertise during its bridge handshake.
 *
 * Capability negotiation is deliberately additive: absence means unsupported.
 * That keeps older agents safe and prevents the platform from dispatching work
 * they cannot execute. New names must be added here before the detector accepts
 * and persists them.
 */
public final class BridgeCapability {
    public static final String DEVICE_CONTROL_V1 = "DEVICE_CONTROL_V1";
    public static final String SCREEN_STREAM_V1 = "SCREEN_STREAM_V1";
    public static final String APP_INSTALL_V1 = "APP_INSTALL_V1";
    public static final String DEVICE_DIAGNOSTICS_V1 = "DEVICE_DIAGNOSTICS_V1";
    public static final String NATIVE_AUTOMATION_V1 = "NATIVE_AUTOMATION_V1";
    public static final String PRIVATE_CONNECTOR_V1 = "PRIVATE_CONNECTOR_V1";

    public static final Set<String> KNOWN = Set.of(
            DEVICE_CONTROL_V1,
            SCREEN_STREAM_V1,
            APP_INSTALL_V1,
            DEVICE_DIAGNOSTICS_V1,
            NATIVE_AUTOMATION_V1,
            PRIVATE_CONNECTOR_V1
    );

    private BridgeCapability() {}
}
