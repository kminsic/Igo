package com.wak.igo.repository;

import com.wak.igo.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public  interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByRequestIdAndNickname(Long Id , String nickname);
    List<Heart> findAllByRequestId(Long RequestID);
    List<Heart> findAllByNickname(String nickname);

}
