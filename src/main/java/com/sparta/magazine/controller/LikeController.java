package com.sparta.magazine.controller;

import com.sparta.magazine.model.Post;
import com.sparta.magazine.model.User;
import com.sparta.magazine.repository.PostRepository;
import com.sparta.magazine.security.UserDetailsImpl;
import com.sparta.magazine.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
public class LikeController {

    private final LikeService likeService;
    private final PostRepository postRepository;

    @GetMapping("/api/post/{post_id}/like")
    public String createLike(@PathVariable Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("좋아요를 하기 위해서는 로그인이 필요합니다.");
        } else {
            User user = userDetails.getUser();
            Optional<Post> optionalPost = postRepository.findById(post_id);
            if (!optionalPost.isPresent()) {
                throw new IllegalArgumentException("해당 postId의 게시물이 존재하지 않습니다.");
            }
            Post post = optionalPost.get();
            // service 에게 작업 넘기고 적절한 메시지 돌려받는다.(좋아요 추가한 건지, 취소한건지)
            String message = likeService.createLike(user, post);
            return message;
        }

    }

}
