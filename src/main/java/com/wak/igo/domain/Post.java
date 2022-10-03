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
//@DisplayName
public class Post extends Timestamped {

    //Json형식으로 받기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @JoinColumn
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    , length = 50000
    private List<Comment> comments;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private String mapData;

//    @Column
//    private String thumnail;

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

    public void addReport() {
        this.reportNum++;
    }

    public void addHeart() {
        this.heartNum++;
    }




    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
//        this.mapData = postRequestDto.getMapData();
//        this.tag = postRequestDto.getTag();
    }
    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}






