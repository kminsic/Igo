package com.wak.igo.service;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import com.wak.igo.dto.request.PostRequestDto;
import com.wak.igo.dto.response.PostResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.repository.MemberRepository;
import com.wak.igo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
//    private final MemberRepository memberRepository;

//    //전체 게시글 조회 (Tag X)
//    @Transactional
//    public ResponseDto<?> getAllPosts() {
//        return ResponseDto.success(postRepository.findAllByPostByCreatedAtDesc());
//    }

    //상세 페이지 조회

//    @Transactional
//    public ResponseDto<?> getPost() {
//
//        Optional<Member> member = memberRepository.findById(1L);
//        Member member1 = member.get();
//
//        String[] sarr = member.getTag().split("#");
//        List<Post> postList = new ArrayList<>();
//        for (String s : sarr) {
//            List<Post> byTagContaining = postRepository.findByTagContaining(s);
//            for (Post post : byTagContaining) {
//                if (postList.contains(post)) {
//                    continue;
//                } else
//                    postList.add(post);
//            }
//        }
//
//        return ResponseDto.success(postList);
//
//    }


    //게시글 생성
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto postRequestDto , MultipartFile multipartFile)throws IOException {
        Post post = Post.builder()
                .title(postRequestDto.getTitle())
//                .image(postRequestDto.getImage())
                .content(postRequestDto.getContent())
                .address(postRequestDto.getAddress())
                .amount(postRequestDto.getAmount())
                .time(postRequestDto.getTime())
//                .viewcount(0)
                //신고하기 기능 구현 x
//                .report(0)
                .tag(postRequestDto.getTag())
                .build();

        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .title(postRequestDto.getTitle())
//                .image(postRequestDto.getImage())
                        .content(postRequestDto.getContent())
                        .address(postRequestDto.getAddress())
                        .amount(postRequestDto.getAmount())
//                        .time(postRequestDto.getTime())
//                .viewcount(0)
                        //신고하기 기능 구현 x
//                .report(0)
                        .tag(postRequestDto.getTag())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build());



    }

    //게시글 수정
//    @Transactional
//    public ResponseDto<Post> updatePost(
//            Long id, PostRequestDto requestDto, MultipartFile multipartFile) throws IOException {
//        Post post = isPresentPost(id);
//        if (null == post) {
//            return ResponseDto.fail("NOT_FOUND", "게시글이 존재하지 않습니다.");}
//    }
//




    //게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long id_post) {
        Post post = isPresentPost(id_post);
        postRepository.delete(post);
        return ResponseDto.success("Success");

    }
    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }
}