package com.wak.igo.repository;

import com.wak.igo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;


@EnableJpaRepositories
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByNickname(String nickname);
//    boolean findAllByNickname(String nickname);

}
