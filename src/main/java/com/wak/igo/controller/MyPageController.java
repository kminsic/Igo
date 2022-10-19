package com.wak.igo.controller;

import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.response.MemberResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    // 회원정보 표시하기
    @GetMapping("/api/mypage")
    public ResponseDto<?> getMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMember(userDetails);
    }

    // 내가 쓴 post 불러오기
    @GetMapping (value = "/api/mypage/post")
    public ResponseDto<?> getPost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getPost(userDetails);
    }

    //좋아요 한 게시글 표시하기
    @GetMapping("/api/mypage/likepost")
    public ResponseDto<?> getHeartpost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getHeartpost(userDetails);
    }

    //회원정보 수정하기
    @PutMapping (value = "/api/mypage/profile")
    public ResponseDto<?> updateMember(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @RequestPart("profileImage") MultipartFile file,
                                       @RequestPart("nickname") MemberResponseDto memberResponseDto
                                       ) throws Exception {
        return myPageService.updateMember(userDetails,file,memberResponseDto);
    }

}