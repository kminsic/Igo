package com.wak.igo.domain;

import javax.persistence.*;


@Entity
public class Category{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_category", nullable = false)
    private Long id;

    @Column
    private String category;

}
