package com.wak.igo.controller;

import com.wak.igo.response.ResponseDto;
import com.wak.igo.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService mypageService;
    @PostMapping(value = "/api/auth/member/info")
    public ResponseDto<?> getAllActs(HttpServletRequest request) {
        return mypageService.getAllActs(request);
    }
}
