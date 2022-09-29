package com.wak.igo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wak.igo.dto.response.MemberResponseDto;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberid;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    private String nickname;

    @Column
    private String profileimage;


    @Convert(converter = StringListConverter.class)
    @Column
    private List<String> interested;



    @Transactional
    public void profileUpdate(MemberResponseDto memberResponseDto, MultipartFile multipartFile) {
        this.nickname = memberResponseDto.getNickname();
        this.profileimage = String.valueOf(multipartFile);

    }

    @Transactional
   public void tagUpdate(List<String> interested) {
        this.interested = interested;
    }

}
