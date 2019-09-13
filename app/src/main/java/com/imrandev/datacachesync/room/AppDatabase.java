package com.imrandev.datacachesync.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.imrandev.datacachesync.room.dao.PostDao;
import com.imrandev.datacachesync.room.model.Post;
import com.imrandev.datacachesync.util.Constant;

@Database(entities = {Post.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract PostDao postDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, Constant.DATABASE_NAME)
                    .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}
