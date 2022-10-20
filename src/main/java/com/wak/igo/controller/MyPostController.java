package com.wak.igo.controller;

import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.MyPostRequestDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.MyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPostController {
    private final MyPostService myPostService;

    // 개인 일정 작성
    @RequestMapping(value = "/api/mypost", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseDto<?> scheduleCreate(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestPart(value = "images", required = false) MultipartFile multipartFile,
                                         @RequestPart(value = "content", required = false) MyPostRequestDto requestDto)
            throws IOException{  // @RequestPart 애너테이션을 이용해서 multipart/form-data 요청받음
        return myPostService.createSchedule(userDetails, multipartFile, requestDto);
    }

    // 일정 수정
    @PatchMapping( "/api/mypost")
    public ResponseDto<?> scheduleUpdate(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestPart(value = "images", required = false) MultipartFile multipartFile,
                                         @RequestPart(value = "content", required = false) MyPostRequestDto requestDto)
            throws IOException{
        return myPostService.updateSchedule(userDetails, multipartFile, requestDto);
    }

    // 일정 삭제
    @DeleteMapping("/api/mypost/{id}")
    public ResponseDto<?> scheduleDelete(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable Long id) {
        return myPostService.deleteSchedule(userDetails, id);
    }

    // 일정 전체 조회
    @GetMapping("/api/mypost")
    public List<?> scheduleGet(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPostService.getSchedule(userDetails);
    }

    // 일정 완료상태로 등록
    @PostMapping("/api/mypost/done/{id}")
    public ResponseDto<?> scheduleDone(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @PathVariable Long id) {
        return myPostService.doneSchedule(userDetails, id);
    }

    // 일정 완료설정 취소
    @PostMapping( "/api/mypost/cancel/{id}")
    public ResponseDto<?> scheduleCancel(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable Long id) {
        return myPostService.cancelSchedule(userDetails, id);
    }
}
