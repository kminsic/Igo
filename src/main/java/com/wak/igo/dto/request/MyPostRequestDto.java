package com.wak.igo.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Builder
public class MyPostRequestDto {
    private Long id;
    private String title;
    private String time;
    private String content;
}
