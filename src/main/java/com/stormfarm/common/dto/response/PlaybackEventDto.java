package com.stormfarm.common.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PlaybackEventDto {
    private String type;
    private String status;
    private int stepIndex;
    private int totalSteps;
    private String stepType;
    private String description;
    private boolean success;
    private String message;
}
