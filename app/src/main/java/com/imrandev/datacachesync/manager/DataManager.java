package com.imrandev.datacachesync.manager;

import com.imrandev.datacachesync.room.model.Post;

public class DataManager {
    private static DataManager instance;

    public static DataManager getInstance(){
        if (instance == null){
            instance = new DataManager();
        }
        return instance;
    }

    public Post getPost(String id, String uid){
        return new Post(id, uid,"sunt aut facere repellat provident occaecati excepturi optio reprehenderit","quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto");
    }
}
