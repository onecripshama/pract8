package com.example.pract8;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DogApi {
    @GET("woof.json")
    Call<DogResponse> getRandomDog();
}
