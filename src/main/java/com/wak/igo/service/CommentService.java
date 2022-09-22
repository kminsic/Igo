package com.wak.igo.service;

import com.wak.igo.domain.Comment;
import com.wak.igo.repository.CommentRepository;
import com.wak.igo.request.CommentRequestDto;
import com.wak.igo.response.CommentResponseDto;
import com.wak.igo.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;


    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto) {

//        Member member = validateMember(request);
//        if (null == member) {
//            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//        }

//        Post post = postService.isPresentPost(requestDto.getPostId());
//        if (null == post) {
//            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
//        }

        Comment comment = Comment.builder()
//                .member(member)
//                .post(post)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);
        return ResponseDto.success(
                CommentResponseDto.builder()
//                        .id(comment.getId())
//                        .author(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
    }
}
