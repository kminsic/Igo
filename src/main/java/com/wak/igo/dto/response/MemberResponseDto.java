package com.wak.igo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MemberResponseDto {
    private String nickname;
    private String profileImg;
    private List<String> interested;
}
