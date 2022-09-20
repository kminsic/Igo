package com.wak.igo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wak.igo.service.KakaoUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

//fdb42734830cbb186c8221bf3acdd6c6
//FuvfQecT3uPmfM3wlzF5VxRJU7Iz654F
//http://localhost:8080/kakao/callback
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final KakaoUserService kakaoUserService;

    @RequestMapping(value = "/kakao/callback", method = RequestMethod.GET)
    public void kakaologin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoUserService.kakaologin(code, response);
    }
}