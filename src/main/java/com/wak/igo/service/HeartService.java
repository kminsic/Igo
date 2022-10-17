package com.wak.igo.service;


import com.wak.igo.domain.Heart;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import com.wak.igo.dto.response.HeartResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.jwt.TokenProvider;
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

    private final TokenProvider tokenProvider;
    private final PostRepository postRepository;
    private final HeartRepository heartRepository;
    private final NotificationService notificationService;



    // 좋아요
    @Transactional
    public ResponseDto<?> addHeartPost(Long id , HttpServletRequest request) {

        ResponseDto<?> chkResponse = validateCheck(request);

        if (!chkResponse.isSuccess())
            return chkResponse;

        Member member = (Member) chkResponse.getData();

        Optional<Post> post = postRepository.findById(id);

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


    // 토큰체크
    private ResponseDto<?> validateCheck(HttpServletRequest request) {
        if (null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            return ResponseDto.fail("로그인이 필요합니다.","로그인이 필요합니다.");
        }
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("Token이 유효하지 않습니다.","Token이 유효하지 않습니다.");
        }
        System.out.println(member.getNickname());
        return ResponseDto.success(member);
    }

    // refreshtoken으로 멤버찾기
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication().getMember();
    }



}