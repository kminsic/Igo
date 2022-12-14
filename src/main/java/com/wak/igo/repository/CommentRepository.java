package com.wak.igo.repository;

import com.wak.igo.domain.Comment;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    List<Comment> findAllByPostOrderByCreatedAtDesc(Post post);

    void deleteAllByPost(Post post);
    void deleteAllByMember(Member member);


    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByPostOrderByCreatedAtDesc(Post post);
}