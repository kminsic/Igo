package com.wak.igo.dto.request;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class MyPostRequestDto {
    private String title;
    private int money;
    private String time;
    private String imgurl;
    private String content;
    private int done;
}
