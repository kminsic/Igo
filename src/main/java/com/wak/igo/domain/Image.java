package com.wak.igo.domain;

import com.wak.igo.dto.request.ImageRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String imgUrl;

    public Image(String fileName, String imgUrl){
        this.fileName = fileName;
        this.imgUrl = imgUrl;


    }
    public void update(String fileName, String imgUrl) {
        this.fileName = fileName;
        this.imgUrl = imgUrl;
    }

    public void update(ImageRequestDto imageRequestDto) {
    }
}