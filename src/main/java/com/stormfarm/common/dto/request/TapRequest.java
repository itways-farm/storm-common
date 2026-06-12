package com.stormfarm.common.dto.request;

import jakarta.validation.constraints.NotNull;

public class TapRequest {
    @NotNull
    private Double x;
    @NotNull
    private Double y;

    public Double getX() { return x; }
    public void setX(Double x) { this.x = x; }
    public Double getY() { return y; }
    public void setY(Double y) { this.y = y; }
}
