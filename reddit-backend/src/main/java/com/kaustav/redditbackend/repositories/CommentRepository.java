package com.kaustav.redditbackend.repositories;

import com.kaustav.redditbackend.models.Comment;
import com.kaustav.redditbackend.models.Post;
import com.kaustav.redditbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>
{
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}