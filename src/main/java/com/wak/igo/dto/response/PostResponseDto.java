package com.wak.igo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private int amount;
    private int heartNum;
    private int viewCount;
    private int reportNum;
    private List<String> tags = new ArrayList<>();


    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}