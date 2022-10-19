package com.wak.igo.service;


import com.wak.igo.domain.Heart;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.response.HeartResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.repository.HeartRepository;
import com.wak.igo.repository.PostRepository;
import com.wak.igo.sse.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HeartService {
    private final PostRepository postRepository;
    private final HeartRepository heartRepository;
    private final NotificationService notificationService;

    // 좋아요
    @Transactional
    public ResponseDto<?> addHeartPost(Long id , UserDetailsImpl userDetails) {
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        Optional<Post> post = postRepository.findById(id);
        Member postMember = post.get().getMember();
        if (post.isEmpty()) {
            return ResponseDto.fail("해당 게시글이 존재하지 않습니다.", "해당 게시글이 존재하지 않습니다.)");
        }
        Member member = (Member) chkResponse.getData();
        Optional<Post> post = postRepository.findById(id);
        Post postNotification = postRepository.findByMemberId(member.getId());
        Member postMember = post.get().getMember();
        if (post.isEmpty()) {
            return ResponseDto.fail("해당 게시글이 존재하지 않습니다.", "해당 게시글이 존재하지 않습니다.)");
        }
        Optional<Heart> heart = heartRepository.findByMemberIdAndPostId(member.getId(), post.get().getId());
        if (heart.isEmpty()) {
            heartRepository.save(Heart.builder()
                    .post(post.get())
                    .member(member)
                    .build());
            post.get().addHeart();
            notificationService.sendHeart(postMember,post,"새로운 좋아요가 왔어요 따듯하네요!");
            return ResponseDto.success(
                    HeartResponseDto.builder()
                            .heartNum(post.get().getHeartNum())
                            .build());
        }
        else {
            heartRepository.delete(heart.get());
            post.get().removeHeart();
            return ResponseDto.success("false");
        }
    }
}