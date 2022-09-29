package com.wak.igo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyPostResponseDto {
    private Long id;
    private String title;
    private int money;
    private String time;
    private String imgUrl;
    private String content;
    private int done;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
