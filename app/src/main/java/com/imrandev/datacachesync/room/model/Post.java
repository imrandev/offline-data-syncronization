package com.imrandev.datacachesync.room.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

@Entity(tableName = "post")
public class Post {

    @PrimaryKey
    @SerializedName("id")
    @NonNull
    private String id = UUID.randomUUID().toString();

    @SerializedName("userId")
    @ColumnInfo(name = "userId")
    private String uid;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("body")
    @ColumnInfo(name = "body")
    private String body;


    public Post(){

    }

    @Ignore
    public Post(@NonNull String id, String uid, String title, String body) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.body = body;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
