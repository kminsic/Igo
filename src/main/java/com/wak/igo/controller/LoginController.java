package com.wak.igo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.LoginRequestDto;
import com.wak.igo.dto.request.MemberRequestDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.FormMemberService;
import com.wak.igo.jwt.TokenProvider;
import com.wak.igo.service.KakaoUserService;
import com.wak.igo.service.NaverUserService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.ParseException;

// https://kauth.kakao.com/oauth/authorize?client_id=3d365192ea8ab4f32c7f9c1d7c5688e1&redirect_uri=http://localhost:3000/kakaoloading&response_type=code
    // 카카오 로그인 url - https://kauth.kakao.com/oauth/authorize?client_id=fdb42734830cbb186c8221bf3acdd6c6&redirect_uri=http://localhost:8080/kakao/callback&response_type=code
// 네이버 로그인 url - https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=DmLVvurxVnPCqlnSp0XZ&state=STATE_STRING&redirect_uri=http://localhost:8080/naver/callback
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final FormMemberService formMemberService;
    private final KakaoUserService kakaoUserService;
    private final NaverUserService naverUserService;
    private final TokenProvider tokenProvider;


    //회원가입
    @PostMapping( "/api/member/signup")
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        return formMemberService.createMember(requestDto);
    }

    // 로그인
    @PostMapping ("/api/member/login")
    public ResponseDto<?> login(@RequestBody  LoginRequestDto requestDto, HttpServletResponse response)
            throws IOException{
        return formMemberService.login( requestDto , response);
    }

    // 카카오 로그인
    @GetMapping("/kakao/callback")
    public ResponseDto<?> kakaologin(@RequestParam String code, HttpServletResponse response)
            throws JsonProcessingException {
        return kakaoUserService.kakaologin(code, response);
    }

    // 네이버 로그인
    @GetMapping( "/naver/callback")
    public ResponseDto<?> naverlogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response)
            throws JsonProcessingException {
        return naverUserService.naverlogin(code, state, response);
    }

    // 로그아웃
    @PostMapping( "/api/member/logout")
    public ResponseDto<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return kakaoUserService.logout(userDetails);
    }

    // 액세스 토큰 재발급
    @GetMapping(value = "/refresh")
    public ResponseDto<?> validate(HttpServletRequest request) throws IOException {
        return ResponseDto.success(tokenProvider.validateRefreshToken(request));
    }
    //회원탈퇴
    @DeleteMapping("/api/member/withdrawal/{id}")
    public ResponseDto<?> withdrawal(@PathVariable Long id,  @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{
        return formMemberService.withdrawal(id, userDetails);
    }

}