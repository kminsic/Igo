package com.wak.igo.dto.request;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class MyPostRequestDto {
    private Long id;
    private String title;
    private int money;
    private String time;
    private String content;
}
