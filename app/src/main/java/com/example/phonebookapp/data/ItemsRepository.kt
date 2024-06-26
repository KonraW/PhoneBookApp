package com.example.phonebookapp.data

import kotlinx.coroutines.flow.Flow

interface ItemsRepository {
    fun getAllItemsStream(): Flow<List<Item>>
    fun getItemStream(id: Int): Flow<Item?>
    suspend fun insertItem(item: Item)
    suspend fun deleteItem(item: Item)
    suspend fun updateItem(item: Item)
    suspend fun deleteAllItems(list: List<Item>)
    fun getLastItemId(): Flow<Int?>
    fun getItemsByCategory(category: Category): Flow<List<Item>>
//    fun sortItems()
}