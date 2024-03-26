package com.utcn.StackOverflow.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "posts")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Post {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    public Post(User author, String body, String picturePath) {
        this.author = author;
        this.body = body;
        this.picturePath = picturePath;
        this.timestamp = new Date();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "body")
    private String body;

    @Column(name = "score" )
    private Long score = 0L;

    public String getBody() {
        return body;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Column(name = "picture_path")
    private String picturePath;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Post() {
        this.timestamp = new Date();
    }
}
