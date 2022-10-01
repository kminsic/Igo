package com.wak.igo.domain;

import com.wak.igo.dto.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
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

    @Column(nullable = false) //
    private String content;



    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private int amount;

    @Column
    private String mapData;


    @Column
    private int viewCount;

    @Column
    private int reportNum;

    @Column
    private int heartNum;

    @Convert(converter = StringListConverter.class)
    private List<String> tags = new ArrayList<>();


    public void add_viewCount() {
        this.viewCount++;}

    public void reportNum() {
        this.reportNum++;
    }

    public void addHeart() {
        this.heartNum++;
    }




    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.amount = postRequestDto.getAmount();
//        this.mapData = postRequestDto.getMapData();
//        this.tag = postRequestDto.getTag();
    }
    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}






