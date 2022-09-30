package com.wak.igo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RefreshToken extends Timestamped {
    @Id
    @Column(nullable = false)
    private Long id;

    @JoinColumn(name = "id_member", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String keyValue;

<<<<<<< HEAD

=======
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f
}
