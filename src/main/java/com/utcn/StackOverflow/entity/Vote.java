package com.utcn.StackOverflow.entity;

import jakarta.persistence.*;

@Table(name = "votes")
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private int voteWeight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getVoteWeight() {
        return voteWeight;
    }

    public void setVoteWeight(int voteWeight) {
        this.voteWeight = voteWeight;
    }

    public Vote() {
    }

    public Vote(Post post, User user, int voteWeight) {
        this.post = post;
        this.user = user;
        this.voteWeight = voteWeight;
    }
}
