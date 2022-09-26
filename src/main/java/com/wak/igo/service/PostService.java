package com.wak.igo.service;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import com.wak.igo.dto.request.PostRequestDto;
import com.wak.igo.dto.response.PostResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.jwt.TokenProvider;
import com.wak.igo.repository.MemberRepository;
import com.wak.igo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.jsonwebtoken.io.IOException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;


    //전체 게시글 조회 (Tag X)
    @Transactional
    public ResponseDto<?> getAllPosts(String type) {
        if (type.equals("create")) {
            return ResponseDto.success(postRepository.findAllByOrderByCreatedAtDesc());
        } else if (type.equals("view")) {
            return ResponseDto.success(postRepository.findAllByOrderByViewCountDesc());
        } else if (type.equals("heart")) {
            return ResponseDto.success(postRepository.findAllByOrderByHeartDesc());
        } else
            return ResponseDto.fail("잘못된 URL 입니다.", "잘못된 접근입니다");
    }

    //
//
//    //상세 페이지 조회
    @Transactional
    public ResponseDto<?> getDetail(Long id) {

        Post post = postRepository.findById(id).get();
        post.add_viewCount();
        return ResponseDto.success(
                PostResponseDto.builder()
                        .heart(post.getHeart())
                        .viewCount(post.getViewCount())
                        .imgurl(post.getImgurl())
                        .amount(post.getAmount())
                        .time(post.getTime())
                        //신고하기 기능 구현 x
//                        .report(0)
                        .tag(post.getTag())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build());


    }

    // 처음 추천 페이지
    @Transactional
    public ResponseDto<?> getRecommend(Long id) {

        Member member = memberRepository.findById(1L).get();

        String[] sarr = member.getTag().split("#");
        List<Post> postList = new ArrayList<>();
        for (String s : sarr) {
            List<Post> byTagContaining = postRepository.findByTagContaining(s);
            for (Post post : byTagContaining) {
                if (postList.contains(post)) {
                    continue;
                } else
                    postList.add(post);
            }
        }
        return ResponseDto.success(postList);
    }


    //게시글 생성
    @Transactional

    public ResponseDto<?> createPost(PostRequestDto postRequestDto, HttpServletRequest request  ) throws IOException {

        Member member = validateMember(request);
        if (null ==member){
            return ResponseDto.fail("INVALID TOKEN", "TOKEN이 유효하지않습니다");
        }

        Post post = Post.builder()

                .member(member)
                .imgurl(postRequestDto.getImgurl())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .address(postRequestDto.getAddress())
                .amount(postRequestDto.getAmount())
                .time(postRequestDto.getTime())
                .heart(0)
                .viewCount(0)
                //신고하기 기능 구현 x
//                .report(0)
                .tag(postRequestDto.getTag())
                .build();
        postRepository.save(post);

        return ResponseDto.success(
    //원하시면 추가
                PostResponseDto.builder()
                        .title(postRequestDto.getTitle())
                        .imgurl(postRequestDto.getImgurl())
                        .content(postRequestDto.getContent())
                        .address(postRequestDto.getAddress())
                        .amount(postRequestDto.getAmount())
                        .time(postRequestDto.getTime())
                        .viewCount(0)
                        //신고하기 기능 구현 x
//                .report(0)
                        .tag(postRequestDto.getTag())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build());
    }

    //    게시글 수정
    @Transactional
    public ResponseDto<?> updatePost(
            Long id, PostRequestDto requestDto) throws IOException {
        Post post = isPresentPost(id);

        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "게시글이 존재하지 않습니다.");
        }

        post.update(requestDto);
        return ResponseDto.success("update success");

    }


    //게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long id) {
        //이게 이미 Optional이란 소리인가? Post 부른순간?
        Post post = isPresentPost(id);
        postRepository.delete(post);
        return ResponseDto.success("Success");

    }

    //
    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        System.out.println(tokenProvider.getMemberFromAuthentication().getMember());
        return tokenProvider.getMemberFromAuthentication().getMember();
    }
    //신고기능 미구현
//    @Transactional
//    public ResponseDto<?> getReport(Long id) {
//        if
//
//    }

}