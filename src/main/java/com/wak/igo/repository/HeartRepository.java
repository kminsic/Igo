package com.wak.igo.repository;

import com.wak.igo.domain.Heart;
import com.wak.igo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    List<Heart> findByMember(Member member);
    Heart findBymemberIdAndPostId(Long memberId, Long postId);

}