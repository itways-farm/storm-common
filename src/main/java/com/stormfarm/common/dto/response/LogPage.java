package com.stormfarm.common.dto.response;
import lombok.*;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LogPage {
    private List<LogResponse> content;
    private PageMeta meta;
}
