package com.stormfarm.common.protocol;

/**
 * Values of the {@code type} field of the JSON messages exchanged with the
 * device bridges over {@code /ws/bridge}. The bridges (storm-android-bridge,
 * storm-ios-bridge) mirror these strings; change both sides together.
 */
public final class BridgeMessageType {

    // cloud to bridge
    public static final String WELCOME = "WELCOME";
    public static final String TAP = "TAP";
    public static final String SWIPE = "SWIPE";
    public static final String TEXT = "TEXT";
    public static final String KEYEVENT = "KEYEVENT";
    public static final String START_SCRCPY = "START_SCRCPY";
    public static final String STOP_SCRCPY = "STOP_SCRCPY";
    public static final String INSTALL_APK = "INSTALL_APK";
    public static final String TAKE_SCREENSHOT = "TAKE_SCREENSHOT";
    /** On-demand, bounded and redacted device logs for a Test Lab defect. */
    public static final String CAPTURE_DIAGNOSTICS = "CAPTURE_DIAGNOSTICS";
    public static final String GET_RUNNING_APPS = "GET_RUNNING_APPS";
    public static final String GET_FOREGROUND_APP = "GET_FOREGROUND_APP";
    public static final String RESET_STATE = "RESET_STATE";
    /** iOS only: a physical button WebDriverAgent can press (HOME, LOCK, VOLUME_UP, VOLUME_DOWN). */
    public static final String PRESS_BUTTON = "PRESS_BUTTON";
    /** Run-scoped, allow-listed W3C WebDriver request via the local Appium service. */
    public static final String AUTOMATION_HTTP = "AUTOMATION_HTTP";
    /** Idempotently closes the local Appium session for a device. */
    public static final String STOP_AUTOMATION = "STOP_AUTOMATION";

    // bridge to cloud
    public static final String DEVICE_ADDED = "DEVICE_ADDED";
    public static final String DEVICE_REMOVED = "DEVICE_REMOVED";
    /** Agent version and explicitly supported optional protocol features. */
    public static final String AGENT_CAPABILITIES = "AGENT_CAPABILITIES";
    public static final String SCRCPY_STATUS = "SCRCPY_STATUS";
    public static final String RESPONSE = "RESPONSE";
    public static final String HEARTBEAT = "HEARTBEAT";

    private BridgeMessageType() {}
}
