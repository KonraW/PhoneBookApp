package com.example.phonebookapp.data

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val surname: String,
    val category: String,
    val number: List<String>,
    val numberType: List<String>,//NumberTypes,
    val email: String,
    val notes: String
)

enum class NumberTypes {
    HOME,
    WORK,
    MOBILE,
    OTHER
}

enum class Category(val color: Color) {
    FAMILY(Color.Red),
    FRIENDS(Color.Blue),
    WORK(Color.Green),
    OTHER(Color.Gray)
}