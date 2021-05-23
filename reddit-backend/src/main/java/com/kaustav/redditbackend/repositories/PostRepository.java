package com.kaustav.redditbackend.repositories;

import com.kaustav.redditbackend.models.Post;
import com.kaustav.redditbackend.models.Subreddit;
import com.kaustav.redditbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>
{
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}