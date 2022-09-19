package com.wak.igo.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_story;

    private String videourl;

    //private String storycontent;
}
