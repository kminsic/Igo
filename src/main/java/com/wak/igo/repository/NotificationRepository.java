package com.wak.igo.repository;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiverIdOrderByCreatedAtDesc(Long id);
    void deleteAllByReceiver(Member receiver);

}

