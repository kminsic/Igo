package com.wak.igo.repository;

import com.wak.igo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;


@EnableJpaRepositories
public interface MemberRepository extends JpaRepository<Member, Long> {
<<<<<<< HEAD
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByNickname(String nickname);

=======
    Optional<Member> findByMemberid(String memberid);
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f
}
