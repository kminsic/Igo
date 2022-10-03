package com.wak.igo.service;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.InterestedTagDto;
import com.wak.igo.dto.request.PostRequestDto;
import com.wak.igo.dto.response.PostResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.jwt.TokenProvider;
import com.wak.igo.repository.MemberRepository;
import com.wak.igo.repository.PostRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;




    //전체 게시글 조회
    @Transactional
    public ResponseDto<?> getAllPosts() {
        return ResponseDto.success(postRepository.findAllByOrderByCreatedAtDesc());
    }

    //게시글 조회 (조회수, 좋아요 , 최신순)
    @Transactional
    public ResponseDto<?> getAllGroupPosts(String type) {
        if (type.equals("create")) {
            return ResponseDto.success(postRepository.findAllByOrderByCreatedAtDesc());
        } else if (type.equals("view")) {
            return ResponseDto.success(postRepository.findAllByOrderByViewCountDesc());
        } else if (type.equals("heart")) {
            return ResponseDto.success(postRepository.findAllByOrderByHeartNumDesc());
        } else
            return ResponseDto.fail("잘못된 URL 입니다.", "잘못된 접근입니다");
    }




    //상세 페이지 조회
    @Transactional
    public ResponseDto<?> getDetail(Long id) {

        Post post = isPresentPost(id);
        if(null == post) {
            return ResponseDto.fail("NOT_FOUND", "게시글이 존재하지 않습니다.");
        }

        post.add_viewCount();
        return ResponseDto.success(
                PostResponseDto.builder()
                        .title(post.getTitle())
                        .content(post.getContent())
                        .viewCount(post.getViewCount())
                        .heartNum(post.getHeartNum())
//                        .mapData(post.getMapData())
                        .reportNum(0)
                        .tags(post.getTags())
                        .profile(post.getMember().getProfileImage())
                        .nickname(post.getMember().getNickname())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build());

    }


    // 태그 저장
    public ResponseDto<?> getTag(UserDetailsImpl userDetails, InterestedTagDto tagDto) {
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        Member member = userDetails.getMember();

        List<String> tags = tagDto.getInterested();
        member.tag(tags);
        memberRepository.save(member);
        return ResponseDto.success("저장 완료");
    }
    //게시글 생성
    @Transactional

    public ResponseDto<?> createPost(PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {

        Member member = validateMember(request);

        if (null == member){
            return ResponseDto.fail("INVALID TOKEN", "TOKEN이 유효하지않습니다");
        }

        Post post = Post.builder()
                .member(member)
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
//                .mapData(postRequestDto.getMapData())
                .tags(postRequestDto.getTags())
                .heartNum(0)
                .viewCount(0)
                .reportNum(0)
                .build();
        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()

                        .title(postRequestDto.getTitle())
                        .content(postRequestDto.getContent())
//                        .mapData(postRequestDto.getMapData())
                        .tags(postRequestDto.getTags())
                        .reportNum(0)
                        .viewCount(0)
                        .heartNum(0)
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build());
    }

    //    게시글 수정
    @Transactional
    public ResponseDto<?> updatePost(
            Long id, PostRequestDto requestDto,HttpServletRequest request) throws IOException {

        ResponseDto<?> chkResponse = validateCheck(request);
        if (!chkResponse.isSuccess())
            return chkResponse;

        Member member = (Member) chkResponse.getData();
        // 유저 테이블에서 유저객체 가져오기
        Member updateMember = memberRepository.findByNickname(member.getNickname()).get();

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "게시글이 존재하지 않습니다.");
        }
        if (post.validateMember(updateMember))
            return ResponseDto.fail("작성자가 아닙니다.","작성자가 아닙니다.");

        post.update(requestDto);
        return ResponseDto.success("update success");

    }

    //게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {

        ResponseDto<?> chkResponse = validateCheck(request);
        if (!chkResponse.isSuccess())
            return chkResponse;

        Member member = (Member) chkResponse.getData();
        Member updateMember = memberRepository.findByNickname(member.getNickname()).get();

        Post post = isPresentPost(id);
        if (post == null)
            return ResponseDto.fail("글 삭제에 실패하였습니다. (NOT_EXIST)", "글 삭제에 실패하였습니다. (NOT_EXIST)");

        if (post.validateMember(updateMember))
            return ResponseDto.fail("작성자가 아닙니다.", "작성자가 아닙니다.");
        postRepository.delete(post);
        return ResponseDto.success("Success");

    }

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
        return tokenProvider.getMemberFromAuthentication().getMember();
    }
    private ResponseDto<?> validateCheck(HttpServletRequest request) {
        // RefreshToken 및 Authorization 유효성 검사
        if (null == request.getHeader("RefreshToken")
                || null == request.getHeader("Authorization")) {
            return ResponseDto.fail("로그인이 필요합니다.","로그인이 필요합니다.");
        }
        Member member = validateMember(request);
        // token 정보 유효성 검사
        if (null == member) {
            return ResponseDto.fail("Token이 유효하지 않습니다.","Token이 유효하지 않습니다.");
        }
        return ResponseDto.success(member);
    }


}

