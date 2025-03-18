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

    private String imageUrl;

    public MarketingPost(){

    }

    public MarketingPost(String imageUrl, int no, String content) {
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
