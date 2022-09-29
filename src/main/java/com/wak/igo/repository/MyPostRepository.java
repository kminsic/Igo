package com.wak.igo.repository;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.MyPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MyPostRepository extends JpaRepository<MyPost, Long> {
    List<MyPost> findByMember(Member member);
}
