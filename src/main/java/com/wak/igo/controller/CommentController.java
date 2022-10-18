package com.wak.igo.controller;

import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.CommentRequestDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    //댓글 조회
    @GetMapping("/api/comments/{id}")
    public ResponseDto<?> searchpostComment(@PathVariable Long id){
        return commentService.searchpostComment(id);
    }

    // 댓글 작성
    @PostMapping("/api/comment")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(requestDto, userDetails);
    }

    // 댓글 수정
    @RequestMapping(value = "/api/comment/{id}", method = RequestMethod.PUT)
    public ResponseDto<?> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
    }

    // 댓글 삭제
    @RequestMapping(value = "/api/comment/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteComment(@PathVariable Long id,
                                        HttpServletRequest request) {
        return commentService.deleteComment(id, request);
    }
}