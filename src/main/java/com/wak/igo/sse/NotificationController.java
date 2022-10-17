package com.wak.igo.sse;


import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.response.ResponseDto;
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
    @GetMapping(value = "/api/member/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestParam(value = "lastEventId", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(userDetails.getId(), lastEventId);
    }

    /**
     * @title 로그인 한 유저의 모든 알림 조회
     */
    @GetMapping("/api/member/notifications")
    public ResponseEntity<NotificationsResponse> notifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(notificationService.findAllById(userDetails.getMember().getId()));
    }

    /**
     * @title 알림 읽음 상태 변경
     */
    @PatchMapping("/api/member/notifications/{id}")
    public ResponseEntity<Void> readNotification(@PathVariable Long id) {
        notificationService.readNotification(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * @title 알림 삭제
     */
    @DeleteMapping("/api/member/notifications/{id}")
    public ResponseDto<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseDto.success("알림이 삭제 되었습니다!");
    }
}
