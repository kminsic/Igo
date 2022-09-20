package com.wak.igo.controller;

import com.wak.igo.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class PostController {

    private final PostService postService;

    // 전체 목록 조회(메인 페이지)
    @GetMapping("/api/post")
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPosts();
    }


    // 게시글 상세 페이지
    @GetMapping("/api/post/{id_post}")
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }


    // 게시글 등록
    // consumes = 클라이언트에게 받을 mediatype를 입력된 내용으로만 제한함
    // requestpart의 value값은 프론트엔드와 맞춰야 입력값이 제대로 들어옴
    @PostMapping(value = "/api/post", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> createPost(@RequestPart(value = "title") PostRequestDto postRequestDto,
                                     @RequestPart(value = "imageUrl", required = false) MultipartFile multipartFile,
                                     HttpServletRequest request) throws IOException {
        return postService.createPost(postRequestDto, multipartFile, request);
    }


    // 게시글 수정
    @CrossOrigin
    @PatchMapping(value = "/api/post/{id}")
    public ResponseDto<?> updatePost(@PathVariable Long id,
                                     @RequestPart(value = "title") PostRequestDto postRequestDto,
                                     @RequestPart(value = "imageUrl", required = false) MultipartFile multipartFile,
                                     HttpServletRequest request) throws IOException {
        return postService.updatePost(id, postRequestDto, multipartFile, request);
    }


    // 게시글 삭제
    @DeleteMapping("/api/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }

}
}
