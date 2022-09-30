package com.wak.igo.controller;

import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    //좋아요 누르기
    @PostMapping("/api/heart/{id}")
    public ResponseDto<?> addHeartPost(@PathVariable Long id, HttpServletRequest request) {
        return heartService.addHeartPost(id, request);
    }

    // 좋아요 취소
    @PostMapping("/api/unheart/{id}")
    public ResponseDto<?> removeHeartPost(@PathVariable Long id, HttpServletRequest request) {
        return heartService.removeHeartPost(id, request);
    }

}