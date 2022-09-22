package com.wak.igo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//
//    @Column(nullable = false)
////    @JsonIgnore
//    private String password;
//
//    @Column
//    private String nickname;
//
//    @Column
//    private String profileimage;
//
//    @Column
//    private String tag;



//    @JoinColumn(name = "id_category", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Tag tag;


}
