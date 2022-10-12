package com.wak.igo.service;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import com.wak.igo.domain.Report;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.jwt.TokenProvider;
import com.wak.igo.repository.PostRepository;
import com.wak.igo.repository.ReportRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    private final TokenProvider tokenProvider;
    private final PostRepository postRepository;

    //신고하기
    @Transactional
    public ResponseDto<?> insertReport(Long id, HttpServletRequest request  ) throws IOException {
        ResponseDto<?> chkResponse = validateCheck(request);
        if (!chkResponse.isSuccess())
            return chkResponse;

        Member member = (Member) chkResponse.getData();
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty())
            return ResponseDto.fail("해당 게시글이 존재하지 않습니다.", "해당 게시글이 존재하지 않습니다.");

        if(!reportRepository.findByMemberIdAndPostId(member.getId(),post.get().getId()).isEmpty()) {
            return ResponseDto.fail("이미 신고한 글 입니다.", "이미 신고한 글 입니다.");}

        Optional<Report> reportPost = reportRepository.findByMemberIdAndPostId(member.getId(), post.get().getId());

        //Test 후 49로 변경 (신고 누적)
        if (reportPost.isPresent()) {
            if (49 >= reportRepository.findAllByPostId(post.get().getId()).size())
                postRepository.deleteById(id);
            //삭제 후 flush로 신고 게시글 업데이트
            reportRepository.flush();
                return ResponseDto.success("삭제된 게시글 입니다.");}
            else
            { Report report = Report.builder()
                        .member(member)
                        .post(post.get())
                        .build();

                reportRepository.save(report);
                post.get().addReport();
                return ResponseDto.success("신고 완료");
            }
    }


    // 토큰 체크
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

    // refreshtoken으로 멤버찾기
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication().getMember();
    }
}
