package com.kaustav.redditbackend.services;

import com.kaustav.redditbackend.dto.SubredditDto;
import com.kaustav.redditbackend.exceptions.SubredditNotFoundException;
import com.kaustav.redditbackend.exceptions.UsernameNotFoundException;
import com.kaustav.redditbackend.mapper.SubredditMapperImpl;
import com.kaustav.redditbackend.models.Subreddit;
import com.kaustav.redditbackend.models.User;
import com.kaustav.redditbackend.repositories.SubredditRepository;
import com.kaustav.redditbackend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.time.Instant.now;

@Service
@AllArgsConstructor
public class SubredditService
{
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final SubredditMapperImpl subredditMapperImpl;
    private final UserRepository userRepository;

    @Transactional
    public List<SubredditDto> getAll()
    {
        return subredditRepository.findAll()
                .stream()
                .map((subreddit) -> subredditMapperImpl.mapSubredditToDto(subreddit))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SubredditDto> getCreatedSubreddits()
    {
        return authService.getCurrentUser().getCreatedSubReddits()
                .stream()
                .map((subreddit) -> subredditMapperImpl.mapSubredditToDto(subreddit))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id)
    {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with id -" + id));
        return subredditMapperImpl.mapSubredditToDto(subreddit);
    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto)
    {
        Subreddit subreddit = subredditMapperImpl.mapDtoToSubreddit(subredditDto);
        subreddit.setCreator(authService.getCurrentUser());
        subreddit.setCreatedDate(now());
        subredditRepository.save(subreddit);
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    @Transactional
    public List<SubredditDto> getSubscribedSubredditsByUserName(String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username - " + username));

        return user.getSubscribedSubReddits()
                .stream()
                .map((subreddit) -> subredditMapperImpl.mapSubredditToDto(subreddit))
                .collect(Collectors.toList());
    }

    @Transactional
    public void subscribe(Long id)
    {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with id -" + id));

        authService.getCurrentUser()
                .getSubscribedSubReddits()
                .add(subreddit);
    }
}