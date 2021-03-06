package com.kaustav.redditbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto
{
    private Long id;
    private String text;
    private Long postId;
    private Instant createdDate;
    private String userName;
}
