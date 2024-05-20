package com.utcn.StackOverflow.DTOs.post;

public class VoteDTO {
    private Long userId;
    private Long postId;

    public VoteDTO(Long userId, Long postId, Integer voteWeight) {
        this.userId = userId;
        this.postId = postId;
        this.voteWeight = voteWeight;
    }

    public VoteDTO() {
    }

    public Integer getVoteWeight() {
        return voteWeight;
    }

    public void setVoteWeight(Integer voteWeight) {
        this.voteWeight = voteWeight;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private Integer voteWeight;
}
