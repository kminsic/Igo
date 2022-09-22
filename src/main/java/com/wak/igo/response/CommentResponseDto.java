package com.wak.igo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long id_comment;
//    private String author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
