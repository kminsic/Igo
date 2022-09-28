package com.wak.igo.dto.response;

//import com.wak.igo.domain.MapData;
//import com.wak.igo.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private int amount;
    private int numOfHeart;
    private int viewCount;
    private String mapData;
//    private List<MapData> mapData;
//    private String tag;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}