package com.wak.igo.controller;

import com.wak.igo.dto.request.PostRequestDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

//    // 전체 목록 조회(메인 페이지)
//    @GetMapping("/api/post")
//    public ResponseDto<?> getAllPosts() {
//        return postService.getAllPosts();
//    }
//

//    // 게시글 상세 페이지
//    @GetMapping("/api/post/{id}")
//    public ResponseDto<?> getPost(@PathVariable Long id) {
//        return postService.getPost(id);
//    }


    // 게시글 등록
    // consumes = 클라이언트에게 받을 mediatype를 입력된 내용으로만 제한함
    // requestpart의 value값은 프론트엔드와 맞춰야 입력값이 제대로 들어옴
    //이미지 받아오는 밸류값 혹시 동영상이 추가될 수 있으니 file로 지정.
    @PostMapping(value = "/api/post")
    public ResponseDto<?> createPost(@RequestPart(value = "post") PostRequestDto postRequestDto,
                                     @RequestPart(value = "file", required = false) MultipartFile multipartFile
                                      ) throws IOException {
        return postService.createPost(postRequestDto, multipartFile);
    }


//    // 게시글 수정
//    @RequestMapping(value = "/api/post/{id}", method = RequestMethod.PUT)
//    public ResponseDto<?> updatePost(@PathVariable Long id,
//                                     @RequestPart(value = "post") PostRequestDto requestDto,
//                                     @RequestPart(value = "file", required = false) MultipartFile multipartFile,
//                                     HttpServletRequest request) throws IOException {
//        return postService.updatePost(id,multipartFile, requestDto, request);
//    }


    // 게시글 삭제
    @DeleteMapping("/api/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id) {
//        postRepository.delete(id);
        return postService.deletePost(id);
    }

}

