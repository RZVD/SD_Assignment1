package com.utcn.StackOverflow.entity;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name="questions")
public class Question extends Post{
    @Column(name = "title")
    private String title;

    @OneToMany
    @JoinColumn(name = "question_post_id")
    private List<Answer> answers = new ArrayList<>();

    public Question(User author, String title, String body, byte[] picturePath, Set<Tag> tags) {
        super(author, body, picturePath);
        this.title = title;
        this.tags = tags;
    }

    public Question(User author, String title, String body, byte[] picturePath) {
        super(author, body, picturePath);
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

    @Setter
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
            "\"score\":"  + "\"" + this.getScore()     + "\"," +
            "\"userScore\":"  + "\"" + this.getAuthor().getScore()     + "\"," +
            "\"author\":"  + "\"" + this.getAuthor().getUsername()     + "\"," +
            "\"authorId\":"  + "\"" + this.getAuthor().getUserId()     + "\"," +
            "\"id\":"     + "\"" + this.getId()   + "\"," +
            "\"text\":"   + "\"" + this.getBody() + "\"," +
            "\"answers\":" + this.answers.stream().sorted(Comparator.comparingLong(Answer::getScore).reversed()).toList() + ","   +
            "\"date\":\"" + this.getTimestamp().toString() + "\"," +
            "\"image\":" + Arrays.toString(this.getPicturePath()) + "," +
            "\"tags\":"   +  this.getTags()
        + "}";
    }

    public List<Answer> getAnswers() {
        return answers;
    }

}
