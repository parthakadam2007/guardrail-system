package com.partha.core.repository;

import com.partha.core.model.Post;
import com.partha.core.model.AuthorType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Get posts by author
    Page<Post> findByAuthorIdAndAuthorType(Long authorId, AuthorType authorType, Pageable pageable);

    // Get all posts (feed)
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Get posts after a timestamp (for infinite scroll / feed)
    Page<Post> findByCreatedAtBeforeOrderByCreatedAtDesc(
            java.time.LocalDateTime time, Pageable pageable);

    @Query("""
    SELECT p FROM Post p 
    LEFT JOIN Comment c ON p.id = c.postId 
    GROUP BY p.id 
    ORDER BY COUNT(c.id) DESC
""")
    Page<Post> findTrendingPosts(Pageable pageable);

    // Count posts by author
    long countByAuthorIdAndAuthorType(Long authorId, AuthorType authorType);
}