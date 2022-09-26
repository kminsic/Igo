package com.wak.igo.repository;

import com.wak.igo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository  extends JpaRepository<Post,Long> {

    List<Post> findByTagContaining (String tag);
    List<Post> findAllByOrderByCreatedAtDesc(); //최신순 정렬
    List<Post> findAllByOrderByViewCountDesc(); //조회수순 정렬
    List<Post> findAllByOrderByHeartDesc(); //좋아요순 정렬
    // 인기도순 정렬
    Optional<Post> findById(Long id);

//    List<Post> findAllByMemberId(Long id);
}