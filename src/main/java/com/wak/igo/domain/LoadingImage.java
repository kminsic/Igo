package com.wak.igo.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class LoadingImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String image;
}
