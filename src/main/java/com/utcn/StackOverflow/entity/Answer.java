package com.utcn.StackOverflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "answers")
public class Answer extends Post{
    public Answer(User author, String body, String picture_path, Question question) {
        super(author, body, picture_path);
        this.question = question;
    }


    public Answer() {
    }

    public Answer(User author, Question question) {
        this.question = question;
        this.setAuthor(author);
    }

    public Answer(Question question) {
        this.question = question;
    }

    @ManyToOne
    private Question question;

    @Override
    public String toString() {
        return "{\"text\":" + "\"" + this.getBody() + "\"," +
                "\"title\":"  + "\"" + this.getScore()    + "\"," +
                "\"userScore\":"  + "\"" + this.getAuthor().getScore()     + "\"," +
                "\"score\":"  + "\"" + this.getScore()     + "\"," +
                "\"author\":"  + "\"" + this.getAuthor().getUsername()     + "\"," +
                "\"id\":"  + "\"" + this.getId()    + "\"," +
                "\"date\":\"" + this.getTimestamp().toString() + "\"}";
    }
}
