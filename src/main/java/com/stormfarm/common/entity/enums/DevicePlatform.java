package com.stormfarm.common.entity.enums;

public enum DevicePlatform {
    ANDROID,
    IOS,
    WEB,
    /** A Windows desktop on a lab host, driven through UI Automation (ITM-0083). */
    WINDOWS,
    /** A Mac on a lab host, driven through Apple's accessibility (Appium Mac2). Add-only. */
    MACOS
}
