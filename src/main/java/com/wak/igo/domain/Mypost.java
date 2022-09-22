package com.wak.igo.domain;

import javax.persistence.*;

public class Mypost extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_member", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private int done;

    @Column
    private int money;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String title;



}
