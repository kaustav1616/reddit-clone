package com.kaustav.redditbackend.dto;

import com.kaustav.redditbackend.models.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto
{
    private VoteType voteType;
    private Long postId;
}