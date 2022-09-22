package com.wak.igo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_post;

    @Column(nullable = false) //
    private String title;

    @Column(nullable = false) //
    private String content;

    @Column(nullable = false)
    private String imgUrl;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private int heart;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String address;

    @Column
    private int viewcount;

    @Column
    private int report;


}
