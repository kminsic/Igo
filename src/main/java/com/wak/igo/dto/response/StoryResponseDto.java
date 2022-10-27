package com.wak.igo.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StoryResponseDto {
    private Long id;
    private String video;
    private String profileImage;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
