package com.wak.igo.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StoryResponseDto {
    private Long id;
    private String video;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
