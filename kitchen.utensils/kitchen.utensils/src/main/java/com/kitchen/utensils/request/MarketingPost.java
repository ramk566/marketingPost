package com.kitchen.utensils.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"No", "content"})

public class MarketingPost {

    @JsonProperty("No")
    private int No;
    private String content;

    public MarketingPost(){

    }

    public MarketingPost(int no, String content) {
        No = no;
        this.content = content;
    }

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
