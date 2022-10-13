package com.wak.igo.sse;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationResponse {
    /**
     * 알림 id
     */
    private Long id;

    /**
     * 알림 내용
     */
    private String content;

    /**
     * 알림 클릭 시 이동할 url
     */
    private String url;

    /**
     * 알림이 생성된 날짜(몇일 전 계산 위함)
     */
    private Integer[] createdAt;

    /**
     * 알림 읽음 여부
     */
    private boolean read;

    @Builder
    public NotificationResponse(Long id, String content, String url, LocalDateTime createdAt, boolean read) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.read = read;
    }

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .url(notification.getUrl())
                .createdAt(notification.getCreatedAt())
                .read(notification.isRead())
                .build();
    }
}
