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

    // 내가 쓴 post 불러오기
    @GetMapping (value = "/api/member/post")
    public ResponseDto<?> getPost(HttpServletRequest request) {
        return myPageService.getPost(request);
    }
    // 나의일정 불러오기
    @GetMapping(value = "/api/member/mypost")
    public ResponseDto<?> getMypost(HttpServletRequest request) {
        return myPageService.getMypost(request);
    }
    // 회원정보 표시하기
    @GetMapping("api/member/mypage")
    public ResponseDto<?> getMember(HttpServletRequest request) {
        return myPageService.getMember(request);
    }
    //좋아요 한 게시글 표시하기
    @GetMapping("api/mypage/post")
    public ResponseDto<?> getHeartpost(HttpServletRequest request) {
        return myPageService.getHeartpost(request);
    }
    //회원정보 수정하기
    @PutMapping (value = "/api/auth/member/mypage")
    public ResponseDto<?> updateMember(HttpServletRequest request,
                                       @RequestPart("profileimage") MultipartFile file,
                                       @RequestPart("nickname") MemberResponseDto memberResponseDto
                                       ) throws IOException {
        return myPageService.updateMember(request,file,memberResponseDto);
    }
    //태그 수정하기
    @PutMapping("api/auth/member/updatetag")
    public ResponseDto<?> updateTag(HttpServletRequest request,@RequestBody MemberResponseDto memberResponseDto
    ) throws IOException {
        return myPageService.updateTag(request,memberResponseDto);
    }

}