package com.wak.igo.service;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import com.wak.igo.dto.response.MyPageResponseDto;
import com.wak.igo.dto.response.PostResponseDto;
import com.wak.igo.jwt.TokenProvider;
import com.wak.igo.repository.PostRepository;
import com.wak.igo.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;


    public ResponseDto<?> getAllActs(HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        //         Post 데이터 수집
        List<Post> postList = postRepository.findAllByMember(member);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for (Post post : postList) {
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
//                            .imgUrl(post.getImgUrl())
//                            .likes(post.getLikes())
//                            .(member.getNickname())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(
                MyPageResponseDto.builder()
                        .postResponseDtoList(postResponseDtoList)
                        .build()
        );
    }
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}

