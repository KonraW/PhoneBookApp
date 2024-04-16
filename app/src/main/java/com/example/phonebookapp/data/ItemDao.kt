package com.example.phonebookapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    //select ordered by name and ignore case
    @Query("SELECT * from items ORDER BY name COLLATE NOCASE ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("DELETE FROM items")
    suspend fun deleteAllItems()

    @Query("SELECT MAX(id) FROM items")
    fun getLastItemId(): Flow<Int?>

    @Query("SELECT * from items WHERE category = :category")
    fun getItemsByCategory(category: Category): Flow<List<Item>>

//    @Query("SELECT * from items ORDER BY name ASC")
//    fun sortItems()
}