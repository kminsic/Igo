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
public class ReportResponseDto {

    private Long id;
    private String content;
    private String nickname;

}
