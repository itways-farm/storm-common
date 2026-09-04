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
    public static final String GET_RUNNING_APPS = "GET_RUNNING_APPS";
    public static final String GET_FOREGROUND_APP = "GET_FOREGROUND_APP";
    public static final String RESET_STATE = "RESET_STATE";

    // bridge to cloud
    public static final String DEVICE_ADDED = "DEVICE_ADDED";
    public static final String DEVICE_REMOVED = "DEVICE_REMOVED";
    public static final String SCRCPY_STATUS = "SCRCPY_STATUS";
    public static final String RESPONSE = "RESPONSE";
    public static final String HEARTBEAT = "HEARTBEAT";

    private BridgeMessageType() {}
}
