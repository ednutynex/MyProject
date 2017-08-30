package net.example.ednutynex.myproject.api;

import net.example.ednutynex.myproject.model.ItemResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ednutynex on 8/30/17.
 */

public interface Service {
    @GET("/search/users?q=language:java+location:lagos")
    Call<ItemResponse> getItems();
}
