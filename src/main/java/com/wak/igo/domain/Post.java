package com.wak.igo.domain;

import com.vladmihalcea.hibernate.type.json.JsonType;
import com.wak.igo.dto.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@TypeDef(name = "json", typeClass = JsonType.class) // Map을 Json 타입으로 컨버팅하여 넣기
public class Post extends Timestamped {

    //Json형식으로 받기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private Integer viewCount;

    @Column
    private Integer reportNum;

    @Column
    private Integer heartNum;

    @Column
    private String thumnail;

    @Column
    private String searchPlace;

    // column json 설정
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Object> mapData;


    @Column(nullable = false)
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
    public void removeHeart() {
        this.heartNum--;
    }

    public void update(PostRequestDto postRequestDto, String thumnail,String content) {
        this.title = postRequestDto.getTitle();
        this.content = content;
        this.thumnail = thumnail;
        this.tags = postRequestDto.getTags();
        this.mapData = postRequestDto.getMapData();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}






