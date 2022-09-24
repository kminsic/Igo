package com.wak.igo.repository;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository  extends JpaRepository<Post,Long> {



//    List<Post> findAllByPostByCreatedAtDesc(); //최신순 정렬
//    List<Post> findAllByPostByViewcountDesc(); //조회수순 정렬
    Optional<Post> findById(Long id);
    List<Post> findAllByMember(Member member);
}