package com.wak.igo.domain;

<<<<<<< HEAD
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
=======
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f

import javax.persistence.*;

@Entity
@Table(name = "heart")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Heart  extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post  post;

    @JsonIgnore
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}