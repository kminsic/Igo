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
        Long member = userDetails.getId();
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return ResponseDto.fail("BAD_REQUEST", "해당 게시글이 존재하지 않습니다.)");
        }
        Post postNotification = post.get();
        Member postMember = post.get().getMember();
        Optional<Heart> heart = heartRepository.findByMemberIdAndPostId(userDetails.getId(), post.get().getId());
        if(!postMember.getId().equals(member))
            notificationService.send(postMember,postNotification,"새로운 좋아요가 왔어요!");
        if (heart.isEmpty()) {
            heartRepository.save(Heart.builder()
                    .post(post.get())
                    .member(userDetails.getMember())
                    .build());
            post.get().addHeart();
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