package com.wak.igo.service;

import com.wak.igo.domain.*;
import com.wak.igo.dto.request.InterestedTagDto;
import com.wak.igo.dto.request.PostRequestDto;
import com.wak.igo.dto.response.CommentResponseDto;
import com.wak.igo.dto.response.PostResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.repository.*;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;
    private final HeartRepository heartRepository;
    private final CommentService commentService;


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
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "게시글이 존재하지 않습니다.");
        }
        post.add_viewCount();
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .viewCount(post.getViewCount())
                        .heartNum(post.getHeartNum())
                        .mapData(post.getMapData())
                        .reportNum(post.getReportNum())
                        .tags(post.getTags())
                        .commentResponseDtoList(commentService.findComment(post))
                        .profile(post.getMember().getProfileImage())
                        .nickname(post.getMember().getNickname())
                        .searchPlace(post.getSearchPlace())
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
        return ResponseDto.success("태그 설정 완료");
    }

    // 태그 기반 회원별 메인 게시글
    public List<Post> getTagPost(UserDetailsImpl userDetails) {
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        List<Post> tagPosts = new ArrayList<>();
        Member member = userDetails.getMember();
        List<Post> posts = postRepository.findAll();

        List<String> tags = member.getInterested(); // 회원이 선택한 태그 목록들
        for (String tag : tags) {
            for (Post post : posts) {
                String tagPost = post.getTags().get(0); // 게시글의 interested 항목만 비교 (index 0)
                if (tag.equals(tagPost)) {
                    tagPosts.add(post);
                }
            }
        }
        return tagPosts;
    }

    //관심사 태그별 정렬
    @Transactional
    public List<Post> getAllInterestTags(String type) {

        List<Post> InterestTagPosts = new ArrayList<>();
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            String tagPost = post.getTags().get(0);
            if (tagPost.equals(type)) {
                InterestTagPosts.add(post);
            }
        }
        return InterestTagPosts;
    }

    //지역 태그별 정렬
    @Transactional
    public List<Post> getAllRegionTags(String type) {
        List<Post> RegionTagPosts = new ArrayList<>();
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            String tagPost = post.getTags().get(1);
            if (tagPost.equals(type)) {
                RegionTagPosts.add(post);

            }
        }
        return RegionTagPosts;
    }

    //비용 태그별 정렬
    @Transactional
    public List<Post> getAllCostTags(String type) {

        List<Post> CostTagPosts = new ArrayList<>();
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            String tagPost = post.getTags().get(2);
            if (tagPost.equals(type)) {
                CostTagPosts.add(post);
            }
        }
        return CostTagPosts;
    }

    //게시글 생성
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto postRequestDto, UserDetailsImpl userDetails  ) throws IOException {
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        // 썸네일 추출
        String thumnail = getThumnail(postRequestDto);

        Post post = Post.builder()
                .member(userDetails.getMember())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .mapData(postRequestDto.getMapData())
                .thumnail(thumnail)
                .tags(postRequestDto.getTags())
                .searchPlace(postRequestDto.getSearchPlace())
                .heartNum(0)
                .viewCount(0)
                .reportNum(0)
                .build();
        postRepository.save(post);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(postRequestDto.getTitle())
                        .content(postRequestDto.getContent())
                        .mapData(postRequestDto.getMapData())
                        .tags(postRequestDto.getTags())
                        .reportNum(0)
                        .viewCount(0)
                        .heartNum(0)
                        .searchPlace(postRequestDto.getSearchPlace())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build());
    }

    //    게시글 수정
    @Transactional
    public ResponseDto<?> updatePost(
            Long id, PostRequestDto requestDto, UserDetailsImpl userDetails  ) throws IOException {
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        // 유저 테이블에서 유저객체 가져오기
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "게시글이 존재하지 않습니다.");}
        if (!userDetails.getId().equals(post.getMember().getId()))
            return ResponseDto.fail("작성자가 아닙니다.", "작성자가 아닙니다.");
        // 썸네일 추출
        String content = requestDto.getContent();
        if (content == null) {
            content = post.getContent();
        }
        String getThumnail = content;
        Pattern pattern = Pattern.compile("(https?://[^>\"']*)");
        Matcher matcher = pattern.matcher(getThumnail);
        String thumnail = (matcher.find()) ? matcher.group(0) : "false";
        post.update(requestDto, thumnail, content);
        return ResponseDto.success("수정 완료");
    }

    //게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long id, UserDetailsImpl userDetails  ) throws IOException {
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 게시글이 없습니다."));

        if (!userDetails.getId().equals(post.getMember().getId()))
            return ResponseDto.fail("작성자가 아닙니다.", "작성자가 아닙니다.");;

        commentRepository.deleteAllByPost(post);
        heartRepository.deleteAllByPost(post);
        reportRepository.deleteAllByPost(post);
        postRepository.delete(post);
        return ResponseDto.success("삭제 완료");

    }

    // 썸네일 추출
    public String getThumnail(PostRequestDto postRequestDto) {
        String getThumnail = postRequestDto.getContent();
        Pattern pattern = Pattern.compile("(https?://[^>\"']*)");
        Matcher matcher = pattern.matcher(getThumnail);
        String thumnail = (matcher.find()) ? matcher.group(0) : "false";
        return thumnail;
    }

    //포스트 검색
    @Transactional
    public List<PostResponseDto> findPost(String content) {
        List<Post> posts = postRepository.findByContent(content);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        if (posts.isEmpty()) return postResponseDtoList;
        for (Post post : posts) {
            postResponseDtoList.add(this.convertEntityToDto(post));
        }
        return postResponseDtoList;
    }

    private PostResponseDto convertEntityToDto(Post post) {
        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .nickname(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .profile(post.getMember().getProfileImage())
                .nickname(post.getMember().getNickname())
                .commentResponseDtoList(commentResponseDtoList)
                .mapData(post.getMapData())
                .tags(post.getTags())
                .thumnail(post.getThumnail())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }


    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }
}

