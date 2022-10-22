package com.wak.igo.service;

import com.wak.igo.domain.Post;
import com.wak.igo.domain.Report;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.ReportRequestDto;
import com.wak.igo.dto.response.ReportResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.repository.PostRepository;
import com.wak.igo.repository.ReportRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    private final PostRepository postRepository;

    //신고하기
    @Transactional
    public ResponseDto<?> insertReport(Long id, ReportRequestDto requestDto, UserDetailsImpl userDetails  ) throws IOException {
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty())
            return ResponseDto.fail("BAD_REQUEST", "해당 게시글이 존재하지 않습니다.");

        if(!reportRepository.findByMemberIdAndPostId(userDetails.getId(),post.get().getId()).isEmpty()) {
            return ResponseDto.fail("BAD_REQUEST", "이미 신고한 글 입니다.");}

        Optional<Report> reportPost = reportRepository.findByMemberIdAndPostId(userDetails.getId(), post.get().getId());

        //Test 후 49로 변경 (신고 누적)
        if (reportPost.isPresent()) {
            if (49 >= reportRepository.findAllByPostId(post.get().getId()).size())
                postRepository.deleteById(id);
            //삭제 후 flush로 신고 게시글 업데이트
            reportRepository.flush();
                return ResponseDto.success("삭제된 게시글 입니다.");}
            else {
                Report report = Report.builder()
                        .member(userDetails.getMember())
                        .post(post.get())
                        .content(requestDto.getContent())
                        .build();

                reportRepository.save(report);
                post.get().addReport();
                return ResponseDto.success(ReportResponseDto.builder()
                        .id(report.getId())
                        .nickname(userDetails.getUsername())
                        .content(report.getContent())
                        .build());
            }
    }
}
