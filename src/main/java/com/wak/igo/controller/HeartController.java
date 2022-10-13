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

    //1번 누르면 좋아요, 2번 누르면 좋아요 취소 
    @PostMapping("/api/heart/{id}")
    public ResponseDto<?> addHeartPost(@PathVariable Long id, HttpServletRequest request) {
        return heartService.addHeartPost(id, request);
    }
}
