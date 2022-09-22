package com.wak.igo.repository;

import com.wak.igo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{



        Optional<Member> findById(Long id);

}

=======
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
@EnableJpaRepositories
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByMemberid(String memberid);
}
>>>>>>> 2b349465c063830008cff61d690f09b746f1c08b
