package com.wak.igo.service;

import com.wak.igo.domain.Heart;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.HeartDto;
import com.wak.igo.dto.response.MemberResponseDto;
import com.wak.igo.dto.response.PostResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.repository.HeartRepository;
import com.wak.igo.repository.MemberRepository;
import com.wak.igo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final MyPostService myPostService;

    //회원정보 불러오기
    @Transactional(readOnly = true)
    public ResponseDto<?> getMember(UserDetailsImpl userDetails) {
        Member member = userDetailsService.findByIdMember(userDetails.getId());
        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();
        memberResponseDtoList.add(MemberResponseDto.builder()
                .nickname(member.getNickname())
                .interested(member.getInterested())
                .profileImage(member.getProfileImage())
                .interested(member.getInterested())
                .build()       
        );
        return ResponseDto.success(memberResponseDtoList);
    }
    //회원정보 업데이트
    @Transactional
    public ResponseDto<?> updateMember(UserDetailsImpl userDetails, MultipartFile multipartFile,
                                       MemberResponseDto memberResponseDto) throws Exception {
        Member member = userDetailsService.findByIdMember(userDetails.getId());

        if(sameNick(memberResponseDto.getNickname())){
            return ResponseDto.fail("SAME NICKNAME", "중복된 닉네임 입니다");
        }else {
             memberResponseDto.getNickname();
        }
        member.profileUpdate(memberResponseDto,myPostService.imageUrl(multipartFile));
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .nickname(memberResponseDto.getNickname())
                        .profileImage(myPostService.imageUrl(multipartFile))
                        .build()
        );
    }

    //포스트 불러오기
    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(UserDetailsImpl userDetails) {
        Member member = userDetailsService.findByIdMember(userDetails.getId());
        //  자신이 쓴 글 전부 가져와서 dto리스트로 저장.
        List<Post> postList = postRepository.findByMember(member);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for (Post post : postList) {
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .nickname(post.getMember().getNickname())
                            .heartNum(post.getHeartNum())
                            .content(post.getContent())
                            .thumnail(post.getThumnail())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(postResponseDtoList);
    }


    //좋아요 한 게시글 불러오기
    @Transactional(readOnly = true)
    public ResponseDto<?> getHeartpost(UserDetailsImpl userDetails) {
        Member member = userDetailsService.findByIdMember(userDetails.getId());
        List<Heart> heart = heartRepository.findByMember(member);
        List<HeartDto> heartDtoList = new ArrayList<>();
        List<PostResponseDto> postList = new ArrayList<>();
        for (Heart heart1:heart){
            heartDtoList.add(
                    HeartDto.builder()
                            .postId(heart1.getPost().getId())
                            .memberId(heart1.getMember().getId())
                            .build()
            );Post post = postRepository.findById(heart1.getPost().getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물을 찾을수 없습니다")
            );
            postList.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .nickname(post.getMember().getNickname())
                            .heartNum(post.getHeartNum())
                            .content(post.getContent())
                            .thumnail(post.getThumnail())
                            .build()
            );
        }
        return ResponseDto.success(postList);
    }

    @Transactional(readOnly = true)
    public boolean sameNick(String nickname){
        return memberRepository.existsByNickname(nickname);
    }



}
