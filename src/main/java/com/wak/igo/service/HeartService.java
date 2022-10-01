package com.wak.igo.service;


import com.wak.igo.domain.Heart;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.jwt.TokenProvider;
import com.wak.igo.repository.HeartRepository;
import com.wak.igo.repository.PostRepository;
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


    // 관심 상품 등록
    @Transactional
    public ResponseDto<?> addHeartPost(Long id, HttpServletRequest request) {
        ResponseDto<?> chkResponse = validateCheck(request);
        if (!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty())
            return ResponseDto.fail("해당 게시글이 존재하지 않습니다.","해당 게시글이 존재하지 않습니다.");
        if (heartRepository.findBymemberIdAndPostId(member.getId(), post.get().getId()) != null) {
            return ResponseDto.fail("이미 좋아요한 게시물입니다.","이미 좋아요한 게시물입니다.");
        }

        Heart heart = Heart.builder()
                .member(member)
                .post(post.get())
                .build();

        heartRepository.save(heart);
        post.get().addHeart();
        return ResponseDto.success(" 좋아요 완료.");
    }

    @Transactional
    public ResponseDto<?> removeHeartPost(Long id, HttpServletRequest request) {
        ResponseDto<?> chkResponse = validateCheck(request);
        if (!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty())
            return ResponseDto.fail("해당 게시글이 존재하지 않습니다.", "해당 게시글이 존재하지 않습니다.");

        Heart heart = heartRepository.findBymemberIdAndPostId(member.getId(), post.get().getId());
        if (heart == null) {
            return ResponseDto.fail("좋아요하지 않은 글입니다.","좋아요하지 않은 글입니다.");
        }
        heartRepository.delete(heart);
        post.get().removeHeart();
        return ResponseDto.success("좋아요 취소 완료.");
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
        return ResponseDto.success(member);
    }

    // refreshtoken으로 유저찾기
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication().getMember();
    }



}