package com.wak.igo.controller;

import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    //1번 누르면 좋아요, 2번 누르면 좋아요 취소
    @PostMapping("/api/heart/{id}")
    public ResponseDto<?> addHeartPost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return heartService.addHeartPost(id, userDetails);
    }
}
