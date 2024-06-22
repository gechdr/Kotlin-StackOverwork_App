package com.example.tugasm7_6958

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "detail") val detail: String,
    @ColumnInfo(name = "effort") val effort: String,
    @ColumnInfo(name = "tags") val tags: String,
    @ColumnInfo(name = "owner") val owner: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "answers") val answers: Int = 0
) {
    override fun toString(): String {
        return "$title - $detail - $effort - $tags - $owner"
    }
}