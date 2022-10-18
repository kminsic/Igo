package com.wak.igo.controller;

import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.ScrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScrollController {
    private final ScrollService scrollService;

    // 최신순 게시글 스크롤 페이지네이션
    @RequestMapping(value = "/api/posts/create/articles", method = RequestMethod.GET)
    public ResponseDto<?> getArticleOrderByDesc(@RequestParam Long lastArticleId, @RequestParam int size) {
        return ResponseDto.success(scrollService.ArticleOrderByDesc(lastArticleId, size));
    }

    // 조회순 게시글 스크롤 페이지네이션
    @RequestMapping(value = "/api/posts/view/articles", method = RequestMethod.GET)
    public ResponseDto<?> getArticleView(@RequestParam int leastArticleViewCount, @RequestParam int size) {
        return ResponseDto.success(scrollService.ArticleViewCount(leastArticleViewCount, size));
    }

    // 좋아요순 게시글 스크롤 페이지네이션
    @RequestMapping(value = "/api/posts/heart/articles", method = RequestMethod.GET)
    public ResponseDto<?> getArticleHeart(@RequestParam int leastArticleHeart, @RequestParam int size) {
        return ResponseDto.success(scrollService.ArticleHeart(leastArticleHeart, size));
    }
}
