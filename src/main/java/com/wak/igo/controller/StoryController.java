//package com.wak.igo.controller;
//
//import com.wak.igo.service.StoryService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//@Controller
//public class StoryController {
//
//    private final StoryService storyService;
//
//
//    @PostMapping("/image-upload")
//    @ResponseBody
//    public String imageUpload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
//        return storyService.upload(multipartFile, "igo", "image");
//    }
//
//    @PostMapping("/video-upload")
//    @ResponseBody
//    public String videoUpload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
//        return storyService.upload(multipartFile, "igo", "video");
//    }
//}