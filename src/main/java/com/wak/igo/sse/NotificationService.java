package com.wak.igo.sse;

import com.wak.igo.domain.Member;
import com.wak.igo.domain.MyPost;
import com.wak.igo.domain.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private static final Map<String, SseEmitter> CLIENTS = new ConcurrentHashMap<>();
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class.getName());


    public NotificationService(EmitterRepository emitterRepository, NotificationRepository notificationRepository) {
        this.emitterRepository = emitterRepository;
        this.notificationRepository = notificationRepository;
    }

    public SseEmitter subscribe(Long memberid, String lastEventId) {
        String id = memberid + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        sendDummyAlert(emitter, id);
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(memberid));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;
    }

    // 더미데이터 / 입장 알림 보내기
    private void sendDummyAlert(SseEmitter emitter, String emitterId) {
        try {
            emitter.send("입장", MediaType.APPLICATION_JSON);
        }
        catch (IOException e) {
            log.info(e.toString());
            deleteById(emitterId);
        }
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            log.error("SSE 연결 오류!", exception);
        }
    }

    //포스트 댓글 알림
    @Transactional
    public void send(Member receiver, Post post, String content) {
//    public void send(Member receiver, String content) {
        Notification notification = createNotification(receiver, post, content);
        String id = String.valueOf(receiver.getId());
        notificationRepository.save(notification);
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }


    //마이포스트 알림
    @Transactional
    public void sendMypost(Member receiver, List<MyPost> myPost, String content) {
        Notification notification = createNotificationM(receiver, myPost, content);
        String id = String.valueOf(receiver.getId());
        notificationRepository.save(notification);
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }

    private Notification createNotification(Member receiver, Post post, String content) {
        return Notification.builder()
                .receiver(receiver)
                .content(content)
                .url("/postdetail/" + post.getId())
                .isRead(false)
                .build();
    }


    //마이포스트
    private Notification createNotificationM(Member receiver, List<MyPost> myPost, String content) {
        return Notification.builder()
                .receiver(receiver)
                .content(content)
                .url("/myinfo" )
                .isRead(false)
                .build();
    }


    // Emitter 지우기
    public void deleteById(String id) {
        CLIENTS.remove(id);
    }



    @Transactional
//    public NotificationsResponse findAllById(UserDetailsImpl loginMember) {
    public NotificationsResponse findAllById(Long loginMember) {
        List<NotificationResponse> responses = notificationRepository.findAllByReceiverId(loginMember).stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
        long unreadCount = responses.stream()
                .filter(notification -> !notification.isRead())
                .count();
        return NotificationsResponse.of(responses, unreadCount);
    }

    @Transactional
    public void readNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 알림입니다."));
        notification.read();
    }
    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
    @Scheduled(cron = "* 30 * * * *")
    public void scheduleNotification(){
        findNotification();
    }
    @Transactional
    public void findNotification() {
        List<Notification> notification = notificationRepository.findAll();
        List<Notification> notificationList = new ArrayList<>();
        for (Notification notification1 : notification) {
            if (notification1.isRead()==true) {
                 notificationList.add(notification1);
                 notificationRepository.deleteById(notification1.getId());
            }
        }LOGGER.info("안녕..알림들..");
    }


}
