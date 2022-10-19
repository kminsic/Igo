package com.wak.igo.sse;

import com.wak.igo.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_notification_to_receiver"))
    private Member receiver;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_notification_to_review"))
//    private Review review;


    private String content;

    private String url;

    private boolean isRead;

    @Builder
    public Notification(Member receiver, String content, String url, boolean isRead) {
        this.receiver = receiver;
        this.content = content;
        this.url = url;
        this.isRead = isRead;
    }

    public void read() {
        this.isRead = true;
    }
}
