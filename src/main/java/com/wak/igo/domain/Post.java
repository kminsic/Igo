package com.wak.igo.domain;

import com.wak.igo.dto.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped {

    //Json형식으로 받기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) //
    private String title;

    @Column(nullable = false) //
    private String content;

    @Column(nullable = false)
    private String imgurl;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    //
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int time;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int heart;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private String tag;



        public void add_viewCount() {
        this.viewCount++;}

        public void update(PostRequestDto postRequestDto) {
            this.title = postRequestDto.getTitle();
            this.content = postRequestDto.getContent();
            this.address = postRequestDto.getAddress();
            this.imgurl = postRequestDto.getImgurl();
            this.time = postRequestDto.getTime();
            this.amount = postRequestDto.getAmount();
            this.tag = postRequestDto.getTag();

        }
}











