package com.wak.igo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor // 필드에 쓴 모든 생성자만 만들어줌
public class MemberInfo {
    @NotBlank
    private String memberId;
    @NotBlank
    private String nickname;
}