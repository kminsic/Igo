package com.wak.igo.repository;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import com.wak.igo.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByMemberIdAndPostId(Long memberId, Long postId);
    List<Report> findAllByPostId(Long postId);

    void deleteAllByPost(Post post);
    void deleteAllByMember(Member member);
}

