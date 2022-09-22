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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) //
    private String title;

    @Column(nullable = false) //
    private String content;

    @Column(nullable = false)
    private String image;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
//
//    @JoinColumn(name = "member_id", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Member member;


    @Column(nullable = false)
    private String address;

    @Column
    private int viewcount;

    @Column
    private int report;

    @Column
    private int heart;

    @Column (nullable = false)
    private int time;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String tag;

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Tag> Tags;

//    @CreationTimestamp
//    @Column
//    private LocalDateTime createdAt = LocalDateTime.now();
//
////    @UpdateTimestamp
//    @Column
//    private LocalDateTime updatedAt = LocalDateTime.now();
    public void add_viewcount() {

        this.viewcount++;

    }
//    public Post(PostRequestDto postRequestDto) {
//        this.title = postRequestDto.getTitle();
//        this.image = postRequestDto.getImage();
//        this.content = postRequestDto.getContent();
//        this.address = postRequestDto.getAddress();
//        this.tag = postRequestDto.getTag();
//        this.time = postRequestDto.getTime();
//        this.amount = postRequestDto.getAmount();
//    }
//
//    public void update(PostRequestDto postRequestDto) {
//        this.title = postRequestDto.getTitle();
//        this.image = postRequestDto.getImage();
//        this.content = postRequestDto.getContent();
//        this.address = postRequestDto.getAddress();
//        this.tag = postRequestDto.getTag();
//        this.time = postRequestDto.getTime();
//        this.amount = postRequestDto.getAmount();
//    }




}
