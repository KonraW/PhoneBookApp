package com.example.phonebookapp.data

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDao): ItemsRepository {
    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun getItemStream(id: Int) = itemDao.getItem(id)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)

    override suspend fun deleteAllItems(list: List<Item>) = itemDao.deleteAllItems()

    override fun getLastItemId(): Flow<Int?> = itemDao.getLastItemId()

    override fun getItemsByCategory(category: Category): Flow<List<Item>> = itemDao.getItemsByCategory(category)

//    override fun sortItems() = itemDao.sortItems()
}