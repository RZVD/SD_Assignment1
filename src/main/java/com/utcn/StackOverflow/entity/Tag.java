package com.utcn.StackOverflow.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @Column(name = "tag_id")
    private String tag;


    public Tag(String tag) {
        this.tag = tag;
        this.questions = new HashSet<>();
    }

    public Tag() {
        this.questions = new HashSet<>();
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    @ManyToMany(mappedBy = "tags")
    private Set<Question> questions;

    @Override
    public String toString() {
        return "\"" + this.tag + "\"";
    }
}
