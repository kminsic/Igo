package com.wak.igo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.KakaoUserService;
import com.wak.igo.service.NaverUserService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// https://kauth.kakao.com/oauth/authorize?client_id=3d365192ea8ab4f32c7f9c1d7c5688e1&redirect_uri=http://localhost:3000/kakaoloading&response_type=code
// 카카오 로그인 url - https://kauth.kakao.com/oauth/authorize?client_id=fdb42734830cbb186c8221bf3acdd6c6&redirect_uri=http://localhost:8080/kakao/callback&response_type=code
// 네이버 로그인 url - https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=DmLVvurxVnPCqlnSp0XZ&state=STATE_STRING&redirect_uri=http://localhost:8080/naver/callback
@RequiredArgsConstructor
@RestController
public class LoginController {
    private final KakaoUserService kakaoUserService;
    private final NaverUserService naverUserService;

    @RequestMapping(value = "/kakao/callback", method = RequestMethod.GET)
    public ResponseDto<String> kakaologin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoUserService.kakaologin(code, response);
    }

    @RequestMapping(value = "/naver/callback", method = RequestMethod.GET)
    public ResponseDto<String> naverlogin(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws JsonProcessingException {
        return naverUserService.naverlogin(code, state, response);
    }

    @RequestMapping(value = "/api/member/logout", method = RequestMethod.POST)
    public ResponseDto<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return kakaoUserService.logout(userDetails);
    }
}