package com.wak.igo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.wak.igo.domain.Heart;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.Mypost;
import com.wak.igo.domain.Post;
import com.wak.igo.dto.HeartDto;
import com.wak.igo.dto.response.MemberResponseDto;
import com.wak.igo.dto.response.MyPostResponseDto;
import com.wak.igo.dto.response.PostResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.jwt.TokenProvider;
import com.wak.igo.repository.HeartRepository;
import com.wak.igo.repository.MemberRepository;
import com.wak.igo.repository.MyPostRepository;
import com.wak.igo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final TokenProvider tokenProvider;
    private final PostRepository postRepository;

    private final MemberRepository memberRepository;
    private final MyPostRepository myPostRepository;

    private final HeartRepository heartRepository;

    //회원정보 불러오기
    @Transactional(readOnly = true)
    public ResponseDto<?> getMember(HttpServletRequest request) {
        // 리프레쉬 토큰 확인
        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        // 리프레시 토큰을 이용해서 유저정보찾기
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID TOKEN", "TOKEN이 유효하지않습니다");
        }
        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();
        memberResponseDtoList.add(
                MemberResponseDto.builder()
                        .nickname(member.getNickname())
                        .profileimage(member.getProfileimage())
                        .build()
        );
        return ResponseDto.success(memberResponseDtoList);
    }
    //회원정보 업데이트
    @Transactional
    public ResponseDto<?> updateMember(HttpServletRequest request, MultipartFile multipartFile, MemberResponseDto memberResponseDto) throws IOException {
        // 리프레쉬 토큰 확인
        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        // 리프레시 토큰을 이용해서 유저정보찾기
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID TOKEN", "TOKEN이 유효하지않습니다");
        }
        member.setNickname(memberResponseDto.getNickname());
        member.setProfileimage(multipartFile.getOriginalFilename());
        memberRepository.save(member);

//        member.profileUpdate(memberResponseDto,multipartFile);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .nickname(memberResponseDto.getNickname())
                        .profileimage(imageUrl(multipartFile))
                        .build()
        );

    }
    //태그수정
    @Transactional
    public ResponseDto<?> updateTag(HttpServletRequest request, MemberResponseDto memberResponseDto)throws IOException{
    // 리프레쉬 토큰 확인
        if(null==request.getHeader("RefreshToken")) {
        return ResponseDto.fail("MEMBER_NOT_FOUND",
                "로그인이 필요합니다.");
    }
    // 리프레시 토큰을 이용해서 유저정보찾기
    Member member = validateMember(request);
        if(null == member) {
        return ResponseDto.fail("INVALID TOKEN", "TOKEN이 유효하지않습니다");
    }

        List<String> tags = memberResponseDto.getInterested();
        member.tagUpdate(tags);
        memberRepository.save(member);
        return ResponseDto.success("태그 수정 완료");
    }



    //포스트 불러오기
    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(HttpServletRequest request) {
        // 리프레쉬 토큰 확인
        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        // 리프레시 토큰을 이용해서 유저정보찾기
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID TOKEN", "TOKEN이 유효하지않습니다");
        };

        // 자신이 쓴 글 기준으로 찾기
        //  자신이 쓴 글 전부 가져와서 dto리스트로 저장.
        List<Post> postList = postRepository.findByMember(member);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for (Post post : postList) {
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(postResponseDtoList);
    }

    //마이포스트 불러오기
    @Transactional(readOnly = true)
    public ResponseDto<?> getMypost(HttpServletRequest request) {
        // 리프레쉬 토큰 확인
        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        // 리프레시 토큰을 이용해서 유저정보찾기
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID TOKEN", "TOKEN이 유효하지않습니다");
        };
        //2.마이 포스트 가져와서 dto리스트로 저장
        List<Mypost> myposts = myPostRepository.findByMember(member);
        List<MyPostResponseDto> myPostResponseDtoList = new ArrayList<>();
        for (Mypost mypost : myposts) {
            myPostResponseDtoList.add(
                    MyPostResponseDto.builder()
                            .id(mypost.getId())
                            .done(mypost.getDone())
                            .money(mypost.getMoney())
                            .time(mypost.getTime())
                            .imgUrl(mypost.getImgUrl())
                            .content(mypost.getContent())
                            .title(mypost.getTitle())
                            .build()
            );
        }
        return ResponseDto.success(myPostResponseDtoList);
    }
    //좋아요 한 게시글 불러오기
    @Transactional(readOnly = true)
    public ResponseDto<?> getHeartpost(HttpServletRequest request) {
        // 리프레쉬 토큰 확인
        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        // 리프레시 토큰을 이용해서 유저정보찾기
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID TOKEN", "TOKEN이 유효하지않습니다");
        };

        List<Heart> heart = heartRepository.findByMember(member);
        List<HeartDto> heartDtoList = new ArrayList<>();
        List<PostResponseDto> postList = new ArrayList<>();
        Post post = new Post();
        for (Heart heart1:heart){
            heartDtoList.add(
                    HeartDto.builder()
                            .postId(heart1.getPost().getId())
                            .memberId(heart1.getMember().getId())
                            .build()
            );post = postRepository.findById(heart1.getPost().getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 유형 찾을수 없음")
            );
            postList.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .build()
            );
        }

        return ResponseDto.success(postList);
    }

    //토큰 검증
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication().getMember();
    }


    public String imageUrl(MultipartFile multipartFile) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename(); // 파일 이름 중복되지 않게 랜덤한 값으로 업로드

        ObjectMetadata objMeta = new ObjectMetadata(); // ObjectMetadata를 통해 파일 사이즈를 ContentLength로 S3에 알려준다
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta); // S3 api 메소드 인 putObject를 이용해 파일 stream을 열어서 s3에 파일 업로드

        return amazonS3.getUrl(bucket, s3FileName).toString(); // S3에 업로드 된 사진 url을 가져온다
    }




}
