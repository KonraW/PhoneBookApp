package com.example.phonebookapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val surname: String,
    val number: List<String>,
    val numberType: List<String>,//NumberTypes,
    val email: String,
    val address: String,
    val notes: String
)

enum class NumberTypes {
    HOME,
    WORK,
    MOBILE,
    OTHER
}