package com.wak.igo.controller;

import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.StoryRequestDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoryController {

    private final StoryService storyService;

    //스토리 조회
    @GetMapping(value = "/api/storys")
    public List<?> getAllStorys() {
        return storyService.getAllStorys();
    }
    //스토리 생성
    @PostMapping(value = "/api/story",consumes = {"multipart/form-data"})
    public ResponseDto<?> createStory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestPart(value = "videos", required = false) MultipartFile multipartFile, StoryRequestDto requestDto) throws java.io.IOException {
        return storyService.createStory(userDetails, multipartFile,requestDto);
    }
    // 스토리 삭제
    @DeleteMapping("/api/story/{id}")
    public ResponseDto<?> deleteStory(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storyService.deleteStory(id,userDetails);
    }

}

