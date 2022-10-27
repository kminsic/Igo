package com.wak.igo.domain;

import com.wak.igo.dto.request.MyPostRequestDto;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@AllArgsConstructor
@Entity
public class MyPost extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String time;

    @Column
    private String imgUrl;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String title;

    public MyPost(){

    }

    public void update(MyPostRequestDto requestDto, String imgUrl){
        this.imgUrl = imgUrl;
        this.time = requestDto.getTime();
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
    }
}
