package com.wak.igo.dto.request;

//import com.wak.igo.domain.MapData;
//import com.wak.igo.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    private String title;
    private String content;
    private int amount;
//    private String mapData;
//    private List<MapData> addressList = new ArrayList<>();
//    private String tag;


    //혹시나 몰라서


}