package com.example.pract8;

import com.google.gson.annotations.SerializedName;

public class DogResponse {
    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }
}
