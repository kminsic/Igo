package com.wak.igo.controller;

import com.wak.igo.domain.Post;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.InterestedTagDto;
import com.wak.igo.dto.request.PostRequestDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.PostService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    // 전체 목록 조회(메인 페이지)
    @GetMapping("/api/post")
    public ResponseDto<?> getAllPosts( ) throws IOException {
        return postService.getAllPosts();
    }


    // 그룹 별 목록 조회(메인 페이지)
    @GetMapping("/api/post/group")
    public ResponseDto<?> getAllGroupPosts(@RequestParam String type) {
        return postService.getAllGroupPosts(type);
    }


    // 로그인 후 태그 설정
    @RequestMapping(value = "/api/member/tag", method = RequestMethod.PUT)
    public ResponseDto<?> getTag(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody InterestedTagDto tagDto) {
        return postService.getTag(userDetails, tagDto);
    }

    // 태그 기반 회원별 메인 게시글
    @RequestMapping(value = "/api/member/posts", method = RequestMethod.GET)
    public ResponseDto<?> getTagPost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Post> tagPosts = postService.getTagPost(userDetails);
        return ResponseDto.success(tagPosts);
    }

    // 게시글 상세 페이지(Post ID)
    @GetMapping("/api/detail/{id}")
    public ResponseDto<?> getDetail(@PathVariable Long id) {
        return postService.getDetail(id);
    }

    // 게시글 등록
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
        return postService.updatePost(id, requestDto,request);
    }

    // 게시글 삭제
    @DeleteMapping("/api/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id,request);
    }

}

