package com.wak.igo.repository;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByMember(Member member);
}
