package com.wak.igo.controller;

import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.ScrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ScrollController {
    private final ScrollService scrollService;

    // 최신순 게시글 스크롤 페이지네이션
    @GetMapping("/api/post/create/articles")
    public ResponseDto<?> getArticleOrderByDesc(@RequestParam Long lastArticleId, @RequestParam int size) {
        return ResponseDto.success(scrollService.ArticleOrderByDesc(lastArticleId, size));
    }

    // 조회순 게시글 스크롤 페이지네이션
    @GetMapping("/api/post/view/articles")
    public ResponseDto<?> getArticleView(@RequestParam int leastArticleViewCount, @RequestParam int size) {
        return ResponseDto.success(scrollService.ArticleViewCount(leastArticleViewCount, size));
    }

    // 좋아요순 게시글 스크롤 페이지네이션
    @GetMapping( "/api/post/heart/articles")
    public ResponseDto<?> getArticleHeart(@RequestParam int leastArticleHeart, @RequestParam int size) {
        return ResponseDto.success(scrollService.ArticleHeart(leastArticleHeart, size));
    }
}
