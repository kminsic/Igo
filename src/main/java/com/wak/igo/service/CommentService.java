package com.wak.igo.service;


import com.wak.igo.domain.Comment;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.CommentRequestDto;
import com.wak.igo.dto.response.CommentResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.repository.CommentRepository;
import com.wak.igo.repository.PostRepository;
import com.wak.igo.sse.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;
    private final UserDetailsServiceImpl userDetailsService;

    //포스트 해당 코멘트 조회
    @Transactional
    public ResponseDto<?> searchpostComment(Long id) {
        Post post = findByPostId(id);
        return ResponseDto.success(findComment(post));
    }

    // 댓글 작성
    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Member member = userDetailsService.findByIdMember(userDetails.getId());
        Post post = findByPostId(requestDto.getPostId());
        Member postMember = post.getMember();
        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);
        //댓글을 작성했을때 포스트 작성 멤버에게 전송
        notificationService.send(postMember, post, "새로운 댓글이 달렸습니다!");
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .nickname(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()

        );
    }


    // 댓글 수정
    @Transactional
    public ResponseDto<?> updateComment(Long id, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comment = findByIdComment(id);
        if (userDetailsService.findByIdMember(userDetails.getId()).equals(comment.getMember().getId())) {
            return ResponseDto.fail("BAD_REQUEST", "올바른 사용자가 아닙니다");
        }
        comment.update(requestDto);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .nickname(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
    }

    // 댓글 삭제
    @Transactional
    public ResponseDto<?> deleteComment(Long id, UserDetailsImpl userDetails) {
        Comment comment = findByIdComment(id);
        if (userDetailsService.findByIdMember(userDetails.getId()).equals(comment.getMember().getId())) {
            return ResponseDto.fail("BAD_REQUEST", "올바른 사용자가 아닙니다");
        }
        commentRepository.delete(comment);
        return ResponseDto.success("success");
    }

    @Transactional(readOnly = true)
    public Post findByPostId(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 포스트입니다."));
    }

    @Transactional(readOnly = true)
    public Comment findByIdComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 코멘트입니다."));
    }

    public List<CommentResponseDto> findComment(Post post) {
        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .profile(comment.getMember().getProfileImage())
                            .nickname(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }
        return commentResponseDtoList;
    }


}

