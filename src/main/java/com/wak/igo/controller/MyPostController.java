package com.wak.igo.controller;

import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.MyPostRequestDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.MyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MyPostController {
    private final MyPostService myPostService;

    @RequestMapping(value = "/api/mypost", method = RequestMethod.POST)
    public ResponseDto<?> createSchedule(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestPart(value = "images", required = false) MultipartFile multipartFile,
                                         @RequestPart(value = "content", required = false)MyPostRequestDto requestDto) throws IOException{  // @RequestPart 애너테이션을 이용해서 multipart/form-data 요청받음
        return myPostService.createSchedule(userDetails, multipartFile, requestDto);
    }
}
