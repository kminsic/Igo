package com.wak.igo.repository;

import com.wak.igo.domain.Member;

import com.wak.igo.domain.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findAllByOrderByCreatedAtDesc();
    List<Story> findByCreatedAt(LocalDateTime localDateTime);
    List<Story> deleteAllById(Long id);
    void deleteAllByMember(Member member);
}
