package com.wak.igo.service;

import com.wak.igo.domain.Post;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrollService {
    private final PostRepository postRepository;
    public List<Post> ArticleOrderByDesc(Long id, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return postRepository.findByIdLessThanOrderByCreatedAtDesc(id, pageRequest);
    }

    public List<Post> ArticleViewCount(int viewCount, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return postRepository.findByViewCountLessThanEqual(viewCount, pageRequest);
    }

    public List<Post> ArticleHeart(int heart, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return postRepository.findByHeartNumLessThanEqual(heart, pageRequest);
    }
}
