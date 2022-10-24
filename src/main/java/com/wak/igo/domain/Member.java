package com.wak.igo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wak.igo.dto.response.MemberResponseDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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

    @Column(nullable = false, unique = true)
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
    public void profileUpdate(MemberResponseDto memberResponseDto, String imgUrl) {
        this.nickname = memberResponseDto.getNickname();
        this.profileImage = imgUrl;

    }
    public void tag(List<String> interested) {
        this.interested = interested;
    }
    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }
}
