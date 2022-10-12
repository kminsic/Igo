package com.wak.igo.sse.dto;



import com.wak.igo.domain.Member;
import com.wak.igo.sse.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    private Member receiver;
    private NotificationType notificationType;
    private String content;
    private String url;

}