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
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "memberid", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false) //
    private String title;

    @Column(nullable = false) //
    private String content;

    @Column(nullable = false)
    private String image;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Column(nullable = false)
    private String address;

    @Column
    private int viewcount;

    @Column
    private int report;

    @Column
    private int heart;

    @Column (nullable = false)
    private int time;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String tag;

    public void add_viewcount() {

        this.viewcount++;

    }





}
