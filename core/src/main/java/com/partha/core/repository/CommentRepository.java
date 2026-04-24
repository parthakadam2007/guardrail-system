package com.partha.core.repository;

import com.partha.core.model.Comment;
import com.partha.core.model.AuthorType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Get all comments for a post (threaded)
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    // Paginated comments
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    // Get replies (depth-based)
    List<Comment> findByPostIdAndDepthLevel(Long postId, int depthLevel);

    // Count comments on post
    long countByPostId(Long postId);

    // Get comments by author
    List<Comment> findByAuthorIdAndAuthorType(Long authorId, AuthorType authorType);
}