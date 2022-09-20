package com.wak.igo.domain;

import javax.persistence.*;

//카테고리 총합

@Entity
public class Category{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_category", nullable = false)
    private Long id;

    //지역 분류
    @Column
    private String capital;

    @Column
    private String kwangwon;

    @Column
    private String chungcheong;

    @Column
    private String gyeongsang;

    @Column
    private String jeolla;

    @Column
    private String jeju;


    //관심사 분류
    @Column
    private String solo;

    @Column
    private String couple;

    @Column
    private String family;


    //가격 분류
    @Column
    private String under10;

    @Column
    private String m1020;

    @Column
    private String m2030;

    @Column
    private String over30;



}
