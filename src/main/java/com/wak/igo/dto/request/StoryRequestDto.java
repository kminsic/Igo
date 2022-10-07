package com.wak.igo.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class StoryRequestDto {
    MultipartFile multipartFile;

        private Long member;
        private MultipartFile video;
    }



