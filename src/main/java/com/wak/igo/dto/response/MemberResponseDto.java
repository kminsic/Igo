package com.wak.igo.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Builder
@Getter
@Component
@NoArgsConstructor
@AllArgsConstructor
//JSON으로 응답할 때 null 값은 제외
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponseDto {
    private String nickname;
    private String profileImage;
    private List<String> interested;
}
