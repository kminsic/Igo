package com.wak.igo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {


    @NotBlank
    @Size(min = 4, max = 12)
    @Pattern(regexp = "[a-zA-Z\\d]*${4,12}", message = "아이디 4~12자의 영,숫자로 입력하셔야 합니다.")
    private String memberId;

    @NotBlank
    @Size(min = 2, max = 10)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*${2,10}", message = "닉네임 2~10자로 입력하셔야 합니다(특수문자 불가).")
    private String nickname;

    @NotBlank
    @Size(min = 4, max = 18)
    @Pattern(regexp = "[a-zA-Z\\d]*${4,18}", message = "4~18자의 영,숫자로 입력하셔야 합니다.")
    private String password;

    @NotBlank
    private String passwordConfirm;
}