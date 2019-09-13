package com.imrandev.datacachesync.network;

import com.imrandev.datacachesync.room.model.Post;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiRepository {

    @FormUrlEncoded
    @POST("/posts")
    Call<Post> post(
            @Field("id") String id,
            @Field("userId") String userId,
            @Field("title") String title,
            @Field("body") String body
    );
}
