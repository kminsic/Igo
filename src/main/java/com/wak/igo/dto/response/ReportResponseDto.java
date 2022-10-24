package com.wak.igo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto {

    private Long id;
    private String nickname;
    private List<String> content = new ArrayList<>();

}
