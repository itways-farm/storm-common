package com.stormfarm.common.dto.request;

import jakarta.validation.constraints.NotNull;

public class SwipeRequest {
    @NotNull
    private Double x1;
    @NotNull
    private Double y1;
    @NotNull
    private Double x2;
    @NotNull
    private Double y2;
    private Integer duration;

    public Double getX1() { return x1; }
    public void setX1(Double x1) { this.x1 = x1; }
    public Double getY1() { return y1; }
    public void setY1(Double y1) { this.y1 = y1; }
    public Double getX2() { return x2; }
    public void setX2(Double x2) { this.x2 = x2; }
    public Double getY2() { return y2; }
    public void setY2(Double y2) { this.y2 = y2; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
}
