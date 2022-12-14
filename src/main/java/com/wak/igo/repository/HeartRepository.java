package com.wak.igo.repository;


import com.wak.igo.domain.Heart;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    List<Heart> findByMember(Member member);
    Optional<Heart> findByMemberIdAndPostId(Long memberId, Long postId);
    void deleteAllByMember(Member member);
    void deleteAllByPost(Post post);

}