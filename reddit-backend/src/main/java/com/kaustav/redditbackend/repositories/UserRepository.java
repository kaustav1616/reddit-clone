package com.kaustav.redditbackend.repositories;

import com.kaustav.redditbackend.models.Subreddit;
import com.kaustav.redditbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByUsername(String username);
}