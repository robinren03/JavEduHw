package com.example.renyanyu;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

@Dao
interface KEntityDao {

    @Query("SELECT * FROM KEntity")
    News[] getAllKEntity();

    @Query("DELETE FROM KEntity")
    void clear();

    @Query("SELECT label FROM KEntity")
    String[] getAllKEntityLabel();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(KEntity... kEntities);

    @Update
    void update(KEntity... kEntities);

    @Delete
    void delete(KEntity... kEntities);

}

@Database(entities = {KEntity.class}, version = 1)
public abstract class AppDB extends RoomDatabase {

    public abstract KEntityDao kEntityDao();

    public static AppDB getAppDB(Context context, String name){
        return  Room.databaseBuilder(context.getApplicationContext(), AppDB.class,
                name).build();
    }

}