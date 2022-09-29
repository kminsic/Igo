package com.wak.igo.repository;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.Mypost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPostRepository extends JpaRepository<Mypost, Long> {
    List<Mypost> findByMember(Member member);
}
