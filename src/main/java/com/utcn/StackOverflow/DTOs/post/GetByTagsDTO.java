package com.utcn.StackOverflow.DTOs.post;

import java.util.List;

public class GetByTagsDTO {
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    private List<String> tags;
}
