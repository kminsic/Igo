package com.wak.igo.repository;

import com.wak.igo.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {
    State findMyPostStateByMyPost_Id(Long id);
}
