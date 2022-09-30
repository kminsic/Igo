package com.wak.igo.repository;

import com.wak.igo.domain.Heart;
<<<<<<< HEAD
=======
import com.wak.igo.domain.Member;
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
<<<<<<< HEAD

    List<Heart> findByMemberId(Long id);

=======
    List<Heart> findByMember(Member member);
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f
    Heart findByMemberIdAndPostId(Long memberId, Long postId);

}