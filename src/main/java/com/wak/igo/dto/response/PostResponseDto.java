package com.wak.igo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private int amount;
    private int heartNum;
    private int viewCount;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}