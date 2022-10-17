package com.wak.igo.controller;

import com.wak.igo.dto.response.MemberResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    // 회원정보 표시하기
    @GetMapping("/api/mypage")
    public ResponseDto<?> getMember(HttpServletRequest request) {
        return myPageService.getMember(request);
    }

    // 내가 쓴 post 불러오기
    @GetMapping (value = "/api/mypage/post")
    public ResponseDto<?> getPost(HttpServletRequest request) {
        return myPageService.getPost(request);
    }

    //좋아요 한 게시글 표시하기
    @GetMapping("/api/mypage/likepost")
    public ResponseDto<?> getHeartpost(HttpServletRequest request) {
        return myPageService.getHeartpost(request);
    }

    //회원정보 수정하기
    @PutMapping (value = "/api/mypage/profile")
    public ResponseDto<?> updateMember(HttpServletRequest request,
                                       @RequestPart("profileImage") MultipartFile file,
                                       @RequestPart("nickname") MemberResponseDto memberResponseDto
                                       ) throws Exception {
        return myPageService.updateMember(request,file,memberResponseDto);
    }

}