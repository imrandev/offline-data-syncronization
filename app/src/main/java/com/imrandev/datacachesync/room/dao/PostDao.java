package com.imrandev.datacachesync.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.imrandev.datacachesync.room.model.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Query("select * from post")
    List<Post> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Post post);

    @Delete
    void delete(Post post);

    @Query("select COUNT(*) from post")
    int count();
}
