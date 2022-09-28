package com.wak.igo.repository;

import com.wak.igo.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    List<Heart> findByMemberId(Long id);

    Heart findByMemberIdAndPostId(Long memberId, Long postId);

}