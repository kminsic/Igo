package com.wak.igo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


@Builder
@Getter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private int heartNum;
    private int viewCount;
    private List<CommentResponseDto> commentResponseDtoList;
    private String nickname;
    private String profile;
    private int reportNum;
    private List<String> tags = new ArrayList<>();
    private Map<String, Object> mapData;
    private String thumnail;
    private String searchPlace;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}