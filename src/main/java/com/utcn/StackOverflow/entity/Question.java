package com.utcn.StackOverflow.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name="questions")
public class Question extends Post{
    @Column(name = "title")
    private String title;

    @OneToMany
    @JoinColumn(name = "question_post_id")
    private List<Answer> answers;

    public Question(User author, String title, String body, String picture_path, Set<Tag> tags) {
        super(author, body, picture_path);
        this.title = title;
        this.tags = tags;
    }

    public Question(User author, String title, String body, String picture_path) {
        super(author, body, picture_path);
        this.title = title;
        this.answers = new ArrayList<Answer>();
        this.tags = new HashSet<>();
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Question() {
        super();
    }

    @ManyToMany
    @JoinTable(
        name = "question_tags",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "{" +
            "\"title\":"  + "\"" + this.title     + "\"," +
            "\"id\":"     + "\"" + this.getId()   + "\"," +
            "\"text\":"   + "\"" + this.getBody() + "\"," +
            "\"answers\":" + this.answers         + ","   +
            "\"date\":\"" + this.getTimestamp().toString() + "\"," +
            "\"tags\":"   +   this.getTags().toString()
        + "}";
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
