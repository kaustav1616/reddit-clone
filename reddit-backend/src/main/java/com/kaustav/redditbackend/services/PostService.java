package com.kaustav.redditbackend.services;

import com.kaustav.redditbackend.dto.PostRequest;
import com.kaustav.redditbackend.dto.PostResponse;
import com.kaustav.redditbackend.exceptions.PostNotFoundException;
import com.kaustav.redditbackend.exceptions.SubredditNotFoundException;
import com.kaustav.redditbackend.mapper.PostMapperImpl;
import com.kaustav.redditbackend.models.Post;
import com.kaustav.redditbackend.models.Subreddit;
import com.kaustav.redditbackend.models.User;
import com.kaustav.redditbackend.repositories.PostRepository;
import com.kaustav.redditbackend.repositories.SubredditRepository;
import com.kaustav.redditbackend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService
{
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapperImpl postMapperImpl;

    public void save(PostRequest postRequest)
    {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));

        postRepository.save(postMapperImpl.map(postRequest, subreddit, authService.getCurrentUser()));
    }

    public List<PostResponse> getAllPosts()
    {
        return postRepository.findAll()
                .stream()
                .map((post) -> postMapperImpl.mapToDto(post))
                .collect(toList());
    }

    public PostResponse getPost(Long id)
    {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));

        return postMapperImpl.mapToDto(post);
    }

    public List<PostResponse> getPostsBySubreddit(Long id)
    {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException(id.toString()));

        List<Post> posts = postRepository.findAllBySubreddit(subreddit);

        return posts
                .stream()
                .map((post) -> postMapperImpl.mapToDto(post))
                .collect(toList());
    }

    public List<PostResponse> getPostsByUsername(String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return postRepository.findByUser(user)
                .stream()
                .map((post) -> postMapperImpl.mapToDto(post))
                .collect(toList());
    }
}