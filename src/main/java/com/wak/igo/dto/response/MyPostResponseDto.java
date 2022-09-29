package com.wak.igo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Builder
@Getter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class MyPostResponseDto {
    private Long id;


    private int done;

    private int money;

    private String time;

    private String imgUrl;

    private String content;

    private String title;
}
