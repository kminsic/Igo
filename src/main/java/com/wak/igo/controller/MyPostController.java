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

    @RequestMapping(value = "/api/mypost", method = RequestMethod.POST)
    public ResponseDto<?> scheduleCreate(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart(value = "images", required = false) MultipartFile multipartFile, @RequestPart(value = "content", required = false) MyPostRequestDto requestDto) throws IOException{  // @RequestPart 애너테이션을 이용해서 multipart/form-data 요청받음
        return myPostService.createSchedule(userDetails, multipartFile, requestDto);
    }


    @RequestMapping(value = "/api/mypost", method = RequestMethod.PATCH)
    public ResponseDto<?> scheduleUpdate(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart(value = "images", required = false) MultipartFile multipartFile, @RequestPart(value = "content", required = false) MyPostRequestDto requestDto) throws IOException{
        return myPostService.updateSchedule(userDetails, multipartFile, requestDto);
    }

    @RequestMapping(value = "/api/mypost/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> scheduleDelete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        return myPostService.deleteSchedule(userDetails, id);
    }

    @RequestMapping(value = "/api/mypost", method = RequestMethod.GET)
    public List<?> scheduleGet(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPostService.getSchedule(userDetails);
    }

    @RequestMapping(value = "/api/mypost/done/{id}", method = RequestMethod.POST)
    public ResponseDto<?> scheduleDone(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        return myPostService.doneSchedule(userDetails, id);
    }

    @RequestMapping(value = "/api/mypost/cancel/{id}", method = RequestMethod.POST)
    public ResponseDto<?> scheduleCancel(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        return myPostService.cancelSchedule(userDetails, id);
    }
}
