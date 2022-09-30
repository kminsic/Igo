package com.wak.igo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Builder
@Getter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private int amount;
    private int heartNum;
    private int viewCount;
<<<<<<< HEAD

=======
>>>>>>> 5898ea08a74e7453b88f705a5433f4feb09c7c0f
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}