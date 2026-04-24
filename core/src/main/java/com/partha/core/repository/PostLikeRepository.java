package com.partha.core.repository;

import com.partha.core.model.PostLike;
import com.partha.core.model.AuthorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostIdAndAuthorIdAndAuthorType(
            Long postId, Long authorId, AuthorType authorType);

    long countByPostId(Long postId);
}