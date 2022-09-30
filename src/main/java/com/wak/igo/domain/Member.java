package com.wak.igo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wak.igo.dto.response.MemberResponseDto;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
@Entity
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profileImage;

    @Convert(converter = StringListConverter.class)
    private List<String> interested = new ArrayList<>();

    @Transactional
    public void profileUpdate(MemberResponseDto memberResponseDto, MultipartFile multipartFile) {
        this.nickname = memberResponseDto.getNickname();
        this.profileImage = String.valueOf(multipartFile);

    }
    public void tag(List<String> interested) {
        this.interested = interested;
    }
}
