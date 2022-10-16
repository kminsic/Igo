package com.wak.igo.controller;

import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.StoryRequestDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoryController {

    private final StoryService storyService;

    @RequestMapping(value = "/api/story", method = RequestMethod.GET)
    public List<?> getAllStorys() throws IOException {
        return storyService.getAllStorys();
    }

    @RequestMapping(value = "/api/story", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseDto<?> createStory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestPart(value = "videos", required = false) MultipartFile multipartFile, StoryRequestDto requestDto) throws IOException, java.io.IOException {
        return storyService.createStory(userDetails, multipartFile,requestDto);
    }

}

