package com.wak.igo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.service.KakaoUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final KakaoUserService kakaoUserService;

    @RequestMapping(value = "/kakao/callback1", method = RequestMethod.GET)
    public void kakaologin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoUserService.kakaologin(code, response);
    }

//    @RequestMapping(value = "/kakao/logout", method = RequestMethod.GET)
//    public void kakaologout(UserDetailsImpl userDetails){
//        kakaoUserService.kakaologout(userDetails);
//    }
}