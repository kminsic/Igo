package com.wak.igo.controller;

import com.wak.igo.domain.Post;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.InterestedTagDto;
import com.wak.igo.dto.request.PostRequestDto;
import com.wak.igo.dto.response.PostResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.PostService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 전체 목록 조회(메인 페이지)
    @GetMapping("/api/posts")
    public ResponseDto<?> getAllPosts( ) throws IOException {
        return postService.getAllPosts();
    }

    // 그룹 별 목록 조회(메인 페이지)
    @GetMapping("/api/posts/group")
    public ResponseDto<?> getAllGroupPosts(@RequestParam String type) {
        return postService.getAllGroupPosts(type);
    }

    // 로그인 후 태그 설정
    @RequestMapping(value = "/api/member/tag", method = RequestMethod.PATCH)
    public ResponseDto<?> getTag(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody InterestedTagDto tagDto) {
        return postService.getTag(userDetails, tagDto);
    }

    // 태그 기반 회원별 메인 게시글
    @RequestMapping(value = "/api/member/posts", method = RequestMethod.GET)
    public ResponseDto<?> getTagPost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Post> tagPosts = postService.getTagPost(userDetails);
        return ResponseDto.success(tagPosts);
    }
    //관심사태그 조회
    @RequestMapping(value = "/api/posts/interest", method = RequestMethod.GET)
    public ResponseDto<?> getAllInterestTags(@RequestParam String type) {
        List<Post> InterestTagPosts = postService.getAllInterestTags(type);
        return ResponseDto.success(InterestTagPosts);
    }
    //지역태그 조회
    @RequestMapping(value = "/api/posts/region", method = RequestMethod.GET)
    public ResponseDto<?> getAllRegionTags(@RequestParam String type) {
        List<Post> RegionTagPosts = postService.getAllRegionTags(type);
        return ResponseDto.success(RegionTagPosts);
    }
    //비용태그 조회
    @RequestMapping(value = "/api/posts/cost", method = RequestMethod.GET)
    public ResponseDto<?> getAllCostTags(@RequestParam String type) {
        List<Post> CostTagPosts = postService.getAllCostTags(type);
        return ResponseDto.success(CostTagPosts);
    }

    //title content 기반 검색
    @GetMapping(value = "/api/search")
    public ResponseDto<?> findPost(@RequestParam(value = "content") String content){
        List<PostResponseDto> findPost = postService.findPost(content);
        return ResponseDto.success(findPost);
    }

    // 게시글 상세 페이지(Post ID)
    @GetMapping("/api/detail/{id}")
    public ResponseDto<?> getDetail(@PathVariable Long id) {
        return postService.getDetail(id);
    }

    // 게시글 등록
    @PostMapping(value = "/api/post")
    public ResponseDto<?> createPost(@RequestBody @Valid PostRequestDto postRequestDto,
                                     HttpServletRequest request) throws IOException {
        return postService.createPost(postRequestDto, request);
    }


    // 게시글 수정
    @RequestMapping(value = "/api/post/{id}", method = RequestMethod.PATCH)
    public ResponseDto<?> updatePost(@PathVariable Long id,
                                     @RequestBody @Valid PostRequestDto requestDto,
                                     HttpServletRequest request) throws IOException {
        return postService.updatePost(id, requestDto,request);
    }

    // 게시글 삭제
    @DeleteMapping("/api/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id,request);
    }

}

