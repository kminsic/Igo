package com.wak.igo.controller;

import com.wak.igo.request.CommentRequestDto;
import com.wak.igo.response.ResponseDto;
import com.wak.igo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("api/comment")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto){
        return commentService.createComment(requestDto);
    }
}
