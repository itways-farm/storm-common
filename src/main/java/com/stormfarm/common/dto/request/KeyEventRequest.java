package com.stormfarm.common.dto.request;

import jakarta.validation.constraints.NotNull;

public class KeyEventRequest {
    @NotNull
    private Integer keycode;

    public Integer getKeycode() { return keycode; }
    public void setKeycode(Integer keycode) { this.keycode = keycode; }
}
