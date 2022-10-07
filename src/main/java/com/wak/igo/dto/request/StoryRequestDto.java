package com.wak.igo.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
@Getter
@Builder
public class StoryRequestDto {
        private Long member;
        private String video;
    }



