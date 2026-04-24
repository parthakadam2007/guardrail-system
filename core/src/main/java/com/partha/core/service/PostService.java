package com.partha.core.service;

import com.partha.core.model.*;
import com.partha.core.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    // 1. Create Post
    public Post createPost(Long authorId, AuthorType authorType, String content) {

        Post post = Post.builder()
                .authorId(authorId)
                .authorType(authorType)
                .content(content)
                .build();

        return postRepository.save(post);
    }

    // 2. Add Comment
    public Comment addComment(Long postId, Long authorId, AuthorType authorType,
                              String content, int depthLevel) {

        // 🔥 Validate post exists
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = Comment.builder()
                .postId(postId)
                .authorId(authorId)
                .authorType(authorType)
                .content(content)
                .depthLevel(depthLevel)
                .build();

        return commentRepository.save(comment);
    }

    // 3. Like Post
    public String likePost(Long postId, Long authorId, AuthorType authorType) {

        // Validate post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<PostLike> existingLike =
                postLikeRepository.findByPostIdAndAuthorIdAndAuthorType(
                        postId, authorId, authorType);

        if (existingLike.isPresent()) {
            return "Already liked";
        }

        PostLike like = PostLike.builder()
                .postId(postId)
                .authorId(authorId)
                .authorType(authorType)
                .build();

        postLikeRepository.save(like);

        return "Post liked successfully";
    }
}