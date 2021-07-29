package com.kaustav.redditbackend.services;

import com.kaustav.redditbackend.dto.VoteDto;
import com.kaustav.redditbackend.exceptions.PostNotFoundException;
import com.kaustav.redditbackend.exceptions.SpringRedditException;
import com.kaustav.redditbackend.models.Post;
import com.kaustav.redditbackend.models.Vote;
import com.kaustav.redditbackend.models.VoteType;
import com.kaustav.redditbackend.repositories.PostRepository;
import com.kaustav.redditbackend.repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class VoteService
{
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public void vote(VoteDto voteDto)
    {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType()))
        {
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + "'d for this post");
        }

        if(voteDto.getVoteType().equals(VoteType.UPVOTE))
            post.setVoteCount(post.getVoteCount() + 1);
        else if(voteDto.getVoteType().equals(VoteType.DOWNVOTE))
            post.setVoteCount(post.getVoteCount() - 1);

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post)
    {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}