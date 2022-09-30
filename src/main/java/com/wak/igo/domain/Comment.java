package com.wak.igo.domain;

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
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "id_post", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @JoinColumn(name = "id_member", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

<<<<<<< HEAD

=======
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f
}