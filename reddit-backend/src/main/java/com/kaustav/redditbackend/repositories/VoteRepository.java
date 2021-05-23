package com.kaustav.redditbackend.repositories;

import com.kaustav.redditbackend.models.Post;
import com.kaustav.redditbackend.models.User;
import com.kaustav.redditbackend.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>
{
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
