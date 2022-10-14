package com.wak.igo.sse;


import com.wak.igo.domain.Member;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.service.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * @title 로그인 한 유저 sse 연결
     */
    @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long id,
                                @RequestParam(value = "lastEventId", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(id, lastEventId);
    }

    /**
     * @title 로그인 한 유저의 모든 알림 조회
     */
    @GetMapping("/notifications/{id}")
    public ResponseEntity<NotificationsResponse> notifications(@AuthenticationPrincipal Long loginMember) {
        return ResponseEntity.ok().body(notificationService.findAllById(loginMember));
    }

    /**
     * @title 알림 읽음 상태 변경
     */
    @PatchMapping("/notifications/{id}")
    public ResponseEntity<Void> readNotification(@PathVariable Long id) {
        notificationService.readNotification(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
