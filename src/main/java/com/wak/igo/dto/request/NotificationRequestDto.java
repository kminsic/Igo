package com.wak.igo.dto.request;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.NotificationType;
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