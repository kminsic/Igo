package com.wak.igo.controller;

import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.StoryRequestDto;
import com.wak.igo.dto.response.MemberResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoryController {

    private final StoryService storyService;


    @RequestMapping(value = "/api/story", method = RequestMethod.GET)
    public List<?> getStory(@AuthenticationPrincipal UserDetailsImpl userDetails, MemberResponseDto responseDto) {
        return storyService.getStory(userDetails, responseDto);
    }

    @RequestMapping(value = "/api/story", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseDto<?> createStory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestPart(value = "videos", required = false) MultipartFile multipartFile, StoryRequestDto requestDto)throws IOException {
//                                      @Valid @PathVariable("storyid") StoryRequestDto id)

        // @RequestPart 애너테이션을 이용해서 multipart/form-data 요청받음
        return storyService.createStory(userDetails, multipartFile,requestDto);
    }
}

