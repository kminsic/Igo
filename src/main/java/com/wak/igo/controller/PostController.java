package com.wak.igo.controller;

import com.wak.igo.dto.request.PostRequestDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.repository.PostRepository;
import com.wak.igo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;


    // 전체 목록 조회(메인 페이지)
    @GetMapping("/api/post")
    public ResponseDto<?> getAllPosts(@RequestParam String type) {
        return postService.getAllPosts(type);
    }




    // 게시글 상세 페이지(Post ID)
    @GetMapping("/api/detail/{id}")
    public ResponseDto<?> getDetail(@PathVariable Long id) {
        return postService.getDetail(id);
    }

    // 게시글 등록
    // requestpart의 value값은 프론트엔드와 맞춰야 입력값이 제대로 들어옴
    @PostMapping(value = "/api/post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request
    ) throws IOException {
        return postService.createPost(postRequestDto, request);
    }


    // 게시글 수정
    @RequestMapping(value = "/api/post/{id}", method = RequestMethod.PUT)
    public ResponseDto<?> updatePost(@PathVariable Long id,
                                     @RequestPart(value = "post") PostRequestDto requestDto,
                                     HttpServletRequest request) throws IOException {
        return postService.updatePost(id, requestDto);
    }




}

