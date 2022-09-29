package com.wak.igo.dto.response;


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
public class MemberResponseDto {
    private String memberid;
    private String nickname;
    private String profileimage;
    private List<String> interested;

}
