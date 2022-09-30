package com.wak.igo.domain;

import com.wak.igo.dto.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
<<<<<<< HEAD
import java.time.LocalDateTime;
import java.util.ArrayList;
=======
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f
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

    @Column(nullable = false)
    private String title;

<<<<<<< HEAD
    @Column(nullable = false) //
    private String content;

=======
    @Column(nullable = false)
    private String content;
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

<<<<<<< HEAD
    @Column(nullable = false)
    private int amount;

    @Column
    private String mapData;

    @Column
    private String tag;
    @Column
    private int viewCount;

//    @Column
//    private int report;
    @Column
    private int heartNum;

    public void add_viewCount() {
        this.viewCount++;
    }
=======
    @Column
    private int viewCount;

    @Column
    private String tag;

    @Column
    private int heartNum;

    @Column(nullable = false)
    private int amount;

    public void add_viewCount() {
        this.viewCount++;}
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f

    public void addHeart() {
        this.heartNum++;
    }
<<<<<<< HEAD
=======

>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f
    public void removeHeart() {
        int tempHeart = this.heartNum - 1;
        if (tempHeart < 0) {
            return;
        }
        this.heartNum = tempHeart;
    }

<<<<<<< HEAD


=======
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f
    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.amount = postRequestDto.getAmount();
<<<<<<< HEAD
//        this.mapData = postRequestDto.getMapData();
//        this.tag = postRequestDto.getTag();



    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
=======
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f
    }
}






