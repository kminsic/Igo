package com.wak.igo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeartDto {
    private String postId;
    private String memberId;
}