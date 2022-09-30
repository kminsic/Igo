package com.wak.igo.domain;

import com.wak.igo.dto.request.MyPostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
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

    public void update(MyPostRequestDto requestDto, String imgUrl){
        this.imgUrl = imgUrl;
        this.time = requestDto.getTime();
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
    }
}
