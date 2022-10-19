package com.wak.igo.repository;

import com.wak.igo.domain.Comment;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    void deleteAllByPost(Post post);


}